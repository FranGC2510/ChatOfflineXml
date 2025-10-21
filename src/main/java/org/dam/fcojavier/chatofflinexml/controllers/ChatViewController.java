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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.dam.fcojavier.chatofflinexml.dataAccess.ConversacionDAO;
import org.dam.fcojavier.chatofflinexml.dataAccess.UsuarioDAO;
import org.dam.fcojavier.chatofflinexml.model.Adjunto;
import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.model.Mensaje;
import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.utils.SesionUsuario;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    @FXML
    private Button attachButton;

    private UsuarioDAO usuarioDAO;
    private ConversacionDAO conversacionDAO;
    private Usuario usuarioLogueado;
    private String destinatarioActual;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final DateTimeFormatter exportFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private File archivoAdjunto;

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
        attachButton.setOnAction(event -> handleAdjuntarArchivo());
        logoutButton.setOnAction(event -> handleCerrarSesion());
    }

    @FXML
    private void handleAdjuntarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo Adjunto");

        String[] extensions = Adjunto.EXTENSIONES_PERMITIDAS.stream()
                                    .map(ext -> "*." + ext)
                                    .toArray(String[]::new);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "Archivos Permitidos", extensions
        );
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(attachButton.getScene().getWindow());

        if (selectedFile != null) {
            this.archivoAdjunto = selectedFile;
            attachButton.setText(selectedFile.getName());
        } else {
            this.archivoAdjunto = null;
            attachButton.setText("Adjuntar");
        }
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
        if ((texto.isBlank() && archivoAdjunto == null) || destinatarioActual == null) return;

        if (chatVBox.getChildren().size() == 1 && chatVBox.getChildren().get(0).getStyleClass().contains("welcome-message")) {
            chatVBox.getChildren().clear();
        }

        Adjunto adjuntoParaMensaje = null;
        if (archivoAdjunto != null) {
            String nombreArchivo = archivoAdjunto.getName();
            String tipoArchivo = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);

            Path mediaDirPath = Paths.get("media");
            Path destinoPath = mediaDirPath.resolve(nombreArchivo);

            adjuntoParaMensaje = new Adjunto(nombreArchivo, tipoArchivo, destinoPath.toString(), archivoAdjunto.length());
            if (!adjuntoParaMensaje.esValido()) {
                new Alert(Alert.AlertType.ERROR, "El archivo adjunto no es válido (revisa tamaño o extensión).").showAndWait();
                archivoAdjunto = null;
                attachButton.setText("Adjuntar");
                return;
            }

            try {
                if (!Files.exists(mediaDirPath)) {
                    Files.createDirectories(mediaDirPath);
                }
                Files.copy(archivoAdjunto.toPath(), destinoPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Archivo copiado a: " + destinoPath.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error al copiar el archivo adjunto: " + e.getMessage()).showAndWait();
                archivoAdjunto = null;
                attachButton.setText("Adjuntar");
                return;
            }
        }

        Mensaje nuevoMensaje = new Mensaje(destinatarioActual, usuarioLogueado.getNombre(), texto, LocalDateTime.now(), adjuntoParaMensaje);
        boolean guardado = conversacionDAO.guardarMensaje(nuevoMensaje, usuarioLogueado.getNombre(), destinatarioActual);

        if (guardado) {
            addMensajeToView(nuevoMensaje);
            messageTextField.clear();

            archivoAdjunto = null;
            attachButton.setText("Adjuntar");

            Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
        } else {
            System.err.println("Error al guardar el mensaje.");
        }
    }

    private void addMensajeToView(Mensaje mensaje) {
        // --- MÉTODO COMPLETAMENTE REESCRITO ---

        // 1. Crear el contenedor TextFlow y aplicar estilos de burbuja
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add("chat-bubble");
        if (mensaje.getRemitente().equals(usuarioLogueado.getNombre())) {
            textFlow.getStyleClass().add("chat-bubble-sent");
        } else {
            textFlow.getStyleClass().add("chat-bubble-received");
        }
        textFlow.setMaxWidth(350);

        // 2. Añadir el contenido del mensaje si existe
        if (mensaje.getContenido() != null && !mensaje.getContenido().isBlank()) {
            Text contentText = new Text(mensaje.getContenido());
            contentText.getStyleClass().add("chat-content"); // <-- AÑADIDO
            textFlow.getChildren().add(contentText);
        }

        // 3. Añadir la información del adjunto si existe
        if (mensaje.getAdjunto() != null) {
            // Añadir un salto de línea si ya había texto
            if (!textFlow.getChildren().isEmpty()) {
                textFlow.getChildren().add(new Text("\n"));
            }

            Text adjuntoText;
            Path adjuntoPath = Paths.get(mensaje.getAdjunto().getRuta());

            // Comprobar si el archivo existe y aplicar estilo si no
            if (Files.exists(adjuntoPath)) {
                adjuntoText = new Text("[Adjunto: " + mensaje.getAdjunto().getNombre() + "]");
            } else {
                adjuntoText = new Text("[Adjunto: " + mensaje.getAdjunto().getNombre() + " (FALTA)]");
                adjuntoText.getStyleClass().add("adjunto-faltante"); // Aplicar estilo solo a este texto
            }
            textFlow.getChildren().add(adjuntoText);
        }

        // 4. Añadir la hora del mensaje
        String formattedTime = " [" + mensaje.getFechaHora().format(formatter) + "]";
        Text timeText = new Text(formattedTime);
        timeText.getStyleClass().add("chat-timestamp"); // Estilo para la hora
        textFlow.getChildren().add(timeText);

        // 5. Añadir el TextFlow al HBox para alineación
        HBox hbox = new HBox(textFlow);
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
        hbox.getStyleClass().add("welcome-message");

        chatVBox.getChildren().add(hbox);
    }

    @FXML
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

        List<String> formatos = Arrays.asList("TXT", "CSV", "ZIP");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("TXT", formatos);
        dialog.setTitle("Exportar Conversación");
        dialog.setHeaderText("Elige el formato para exportar la conversación.");
        dialog.setContentText("Formato:");

        Optional<String> formatoElegido = dialog.showAndWait();

        formatoElegido.ifPresent(formato -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Conversación");
            fileChooser.setInitialFileName("conversacion_" + destinatarioActual + "." + formato.toLowerCase());
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(formato + " files (*." + formato.toLowerCase() + ")", "*." + formato.toLowerCase());
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

            if (file != null) {
                if ("ZIP".equals(formato)) {
                    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file))) {
                        Conversacion conversacion = convOpt.get();

                        // 1. Añadir el archivo de texto de la conversación al ZIP
                        ZipEntry textEntry = new ZipEntry("conversacion_" + destinatarioActual + ".txt");
                        zos.putNextEntry(textEntry);
                        PrintWriter zipWriter = new PrintWriter(zos);
                        conversacion.getMensajes().forEach(msg -> {
                            String linea = String.format("[%s] %s: %s",
                                    msg.getFechaHora().format(exportFormatter),
                                    msg.getRemitente(),
                                    msg.getContenido() != null ? msg.getContenido() : ""
                            );
                            zipWriter.println(linea);
                            if (msg.getAdjunto() != null) {
                                zipWriter.println("    [Adjunto: " + msg.getAdjunto().getNombre() + "]");
                            }
                        });
                        zipWriter.flush();
                        zos.closeEntry();

                        // 2. Añadir los archivos adjuntos al ZIP
                        for (Mensaje mensaje : conversacion.getMensajes()) {
                            if (mensaje.getAdjunto() != null) {
                                Path adjuntoPath = Paths.get(mensaje.getAdjunto().getRuta());
                                if (Files.exists(adjuntoPath)) {
                                    ZipEntry adjuntoEntry = new ZipEntry("media/" + adjuntoPath.getFileName().toString());
                                    zos.putNextEntry(adjuntoEntry);
                                    Files.copy(adjuntoPath, zos);
                                    zos.closeEntry();
                                } else {
                                    System.err.println("Advertencia: El adjunto '" + mensaje.getAdjunto().getNombre() + "' no se encontró en la ruta esperada: " + adjuntoPath);
                                }
                            }
                        }
                        new Alert(Alert.AlertType.INFORMATION, "Conversación exportada a ZIP con éxito.").showAndWait();

                    } catch (IOException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Error al exportar la conversación a ZIP: " + e.getMessage()).showAndWait();
                    }
                } else { // Formatos TXT y CSV existentes
                    try (PrintWriter writer = new PrintWriter(file)) {
                        Conversacion conversacion = convOpt.get();
                        if ("CSV".equals(formato)) {
                            writer.println("Fecha;Remitente;Contenido;Adjunto"); // Cabecera CSV modificada
                            conversacion.getMensajes().forEach(msg -> {
                                String adjuntoNombre = (msg.getAdjunto() != null) ? msg.getAdjunto().getNombre() : "";
                                String linea = String.format("%s;%s;\"%s\";%s",
                                        msg.getFechaHora().format(exportFormatter),
                                        msg.getRemitente(),
                                        msg.getContenido() != null ? msg.getContenido().replace("\"", "\"\"") : "", // Escapar comillas dobles
                                        adjuntoNombre
                                );
                                writer.println(linea);
                            });
                        } else { // Formato TXT
                            conversacion.getMensajes().forEach(msg -> {
                                String linea = String.format("[%s] %s: %s",
                                        msg.getFechaHora().format(exportFormatter),
                                        msg.getRemitente(),
                                        msg.getContenido() != null ? msg.getContenido() : ""
                                );
                                writer.println(linea);
                                if (msg.getAdjunto() != null) {
                                    writer.println("    [Adjunto: " + msg.getAdjunto().getNombre() + "]");
                                }
                            });
                        }
                        new Alert(Alert.AlertType.INFORMATION, "Conversación exportada con éxito.").showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, "Error al guardar el fichero: " + e.getMessage()).showAndWait();
                    }
                }
            }
        });
    }

    @FXML
    private void handleCerrarSesion() {
        SesionUsuario.getInstance().cerrarSesion();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/login-view.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Chat Offline - Inicio de Sesión");
            loginStage.setScene(new Scene(root));
            loginStage.setResizable(false);
            loginStage.show();

            Stage chatStage = (Stage) logoutButton.getScene().getWindow();
            chatStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "No se pudo volver a la pantalla de inicio de sesión.").showAndWait();
        }
    }
}
