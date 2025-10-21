package org.dam.fcojavier.chatofflinexml.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.dam.fcojavier.chatofflinexml.dataAccess.ConversacionDAO;
import org.dam.fcojavier.chatofflinexml.dataAccess.UsuarioDAO;
import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.model.Mensaje;
import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.utils.SesionUsuario;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ChatViewController {

    // --- FXML Fields ---
    @FXML
    private ListView<String> userListView;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private VBox chatVBox;
    @FXML
    private TextField messageTextField;
    @FXML
    private Button sendButton;
    @FXML
    private Button statsButton;
    @FXML
    private Button exportButton;
    @FXML
    private Button logoutButton;

    private UsuarioDAO usuarioDAO;
    private ConversacionDAO conversacionDAO;
    private Usuario usuarioLogueado;
    private String destinatarioActual;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter exportFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void initialize() {
        this.usuarioDAO = new UsuarioDAO();
        this.conversacionDAO = new ConversacionDAO();
        this.usuarioLogueado = SesionUsuario.getInstance().getUsuarioActual();

        cargarUsuarios();
        configurarListeners();

        statsButton.setDisable(true);
        exportButton.setDisable(true);
    }

    private void cargarUsuarios() {
        userListView.getItems().setAll(
            usuarioDAO.getUsuariosLista().getUsuarios().stream()
                    .map(Usuario::getNombre)
                    .filter(nombre -> !nombre.equals(usuarioLogueado.getNombre()))
                    .collect(Collectors.toList())
        );
    }

    private void configurarListeners() {
        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinatarioActual = newSelection;
                cargarConversacion(destinatarioActual);
                statsButton.setDisable(false);
                exportButton.setDisable(false);
            }
        });

        sendButton.setOnAction(event -> enviarMensaje());
        messageTextField.setOnAction(event -> enviarMensaje());
        statsButton.setOnAction(event -> abrirVentanaEstadisticas());
        exportButton.setOnAction(event -> handleExportarConversacion());
        //logoutButton.setOnAction(event -> handleCerrarSesion());
    }

    private void cargarConversacion(String destinatario) {
        chatVBox.getChildren().clear();
        Optional<Conversacion> convOpt = conversacionDAO.buscarConversacion(usuarioLogueado.getNombre(), destinatario);
        if (convOpt.isPresent() && !convOpt.get().getMensajes().isEmpty()) {
            convOpt.get().getMensajes().forEach(this::addMensajeToView);
        } else {
            mostrarMensajeBienvenida(destinatario);
        }
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    private void enviarMensaje() {
        String texto = messageTextField.getText();
        if (texto.isBlank() || destinatarioActual == null) return;
        if (chatVBox.getChildren().size() == 1 && chatVBox.getChildren().get(0).getStyleClass().contains("welcome-message")) {
            chatVBox.getChildren().clear();
        }
        Mensaje nuevoMensaje = new Mensaje(destinatarioActual, usuarioLogueado.getNombre(), texto, LocalDateTime.now(), null);
        boolean guardado = conversacionDAO.guardarMensaje(nuevoMensaje, usuarioLogueado.getNombre(), destinatarioActual);
        if (guardado) {
            addMensajeToView(nuevoMensaje);
            messageTextField.clear();
            Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
        } else {
            System.err.println("Error al guardar el mensaje.");
        }
    }

    private void addMensajeToView(Mensaje mensaje) {
        String formattedTime = mensaje.getFechaHora().format(formatter);
        String labelText = String.format("%s [%s]", mensaje.getContenido(), formattedTime);
        Label label = new Label(labelText);
        label.setWrapText(true);
        label.setMaxWidth(350);

        label.getStyleClass().add("chat-bubble");
        if (mensaje.getRemitente().equals(usuarioLogueado.getNombre())) {
            label.getStyleClass().add("chat-bubble-sent");
        } else {
            label.getStyleClass().add("chat-bubble-received");
        }

        // El HBox se usa SOLO para alinear el Label a la derecha o izquierda
        HBox hbox = new HBox(label);
        if (mensaje.getRemitente().equals(usuarioLogueado.getNombre())) {
            hbox.setAlignment(Pos.CENTER_RIGHT);
        } else {
            hbox.setAlignment(Pos.CENTER_LEFT);
        }

        chatVBox.getChildren().add(hbox);
        VBox.setMargin(hbox, new Insets(2, 0, 2, 0));
    }

    private void mostrarMensajeBienvenida(String destinatario) {
        Label label = new Label("¡Aún no hay mensajes! Sé el primero en saludar a " + destinatario + ".");
        HBox hbox = new HBox(label);
        hbox.setAlignment(Pos.CENTER);
        // La clase 'welcome-message' en el HBox aplicará el estilo al Label hijo gracias al CSS
        hbox.getStyleClass().add("welcome-message");

        chatVBox.getChildren().add(hbox);
    }

    /**
     * Abre la ventana de estadísticas para la conversación actual.
     */
    private void abrirVentanaEstadisticas() {
        if (destinatarioActual == null) return;
        Optional<Conversacion> convOpt = conversacionDAO.buscarConversacion(usuarioLogueado.getNombre(), destinatarioActual);
        if (convOpt.isEmpty() || convOpt.get().getMensajes().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Aún no hay mensajes en esta conversación para analizar.").showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/EstadisticasView.fxml"));
            Parent root = loader.load();
            EstadisticasController controller = loader.getController();
            controller.initData(convOpt.get());
            Stage stage = new Stage();
            stage.setTitle("Estadísticas de la conversación con " + destinatarioActual);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(statsButton.getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir la ventana de estadísticas.").showAndWait();
        }
    }

    private void handleExportarConversacion() {
        if (destinatarioActual == null) return;

        Optional<Conversacion> convOpt = conversacionDAO.buscarConversacion(usuarioLogueado.getNombre(), destinatarioActual);
        if (convOpt.isEmpty() || convOpt.get().getMensajes().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No hay nada que exportar.").showAndWait();
            return;
        }

        // 1. Preguntar al usuario por el formato
        List<String> formatos = Arrays.asList("TXT", "CSV");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("TXT", formatos);
        dialog.setTitle("Exportar Conversación");
        dialog.setHeaderText("Elige el formato para exportar la conversación.");
        dialog.setContentText("Formato:");

        Optional<String> formatoElegido = dialog.showAndWait();

        formatoElegido.ifPresent(formato -> {
            // 2. Abrir el FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Conversación");
            fileChooser.setInitialFileName("conversacion_" + destinatarioActual + "." + formato.toLowerCase());
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(formato + " files (*." + formato.toLowerCase() + ")", "*." + formato.toLowerCase());
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

            if (file != null) {
                // 3. Escribir en el fichero
                try (PrintWriter writer = new PrintWriter(file)) {
                    Conversacion conversacion = convOpt.get();
                    if ("CSV".equals(formato)) {
                        writer.println("Fecha;Remitente;Contenido"); // Cabecera CSV
                        conversacion.getMensajes().forEach(msg -> {
                            String linea = String.format("%s;%s;\"%s\"",
                                    msg.getFechaHora().format(exportFormatter),
                                    msg.getRemitente(),
                                    msg.getContenido().replace("\"", "\"\"") // Escapar comillas dobles
                            );
                            writer.println(linea);
                        });
                    } else { // Formato TXT
                        conversacion.getMensajes().forEach(msg -> {
                            String linea = String.format("[%s] %s: %s",
                                    msg.getFechaHora().format(exportFormatter),
                                    msg.getRemitente(),
                                    msg.getContenido()
                            );
                            writer.println(linea);
                        });
                    }
                    new Alert(Alert.AlertType.INFORMATION, "Conversación exportada con éxito.").showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error al guardar el fichero.").showAndWait();
                }
            }
        });
    }

    /**
     * Cierra la sesión del usuario y vuelve a la pantalla de login.
     */
    @FXML
    private void handleCerrarSesion() {
        // 1. Limpiar la sesión actual
        SesionUsuario.getInstance().cerrarSesion();

        try {
            // 2. Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/login-view.fxml"));
            Parent root = loader.load();

            // 3. Crear y mostrar la nueva ventana de login
            Stage loginStage = new Stage();
            loginStage.setTitle("Chat Offline - Inicio de Sesión");
            loginStage.setScene(new Scene(root));
            loginStage.setResizable(false);
            loginStage.show();

            // 4. Cerrar la ventana actual del chat
            Stage chatStage = (Stage) logoutButton.getScene().getWindow();
            chatStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo volver a la pantalla de inicio de sesión.").showAndWait();
        }
    }
}