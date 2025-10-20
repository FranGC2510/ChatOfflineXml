package org.dam.fcojavier.chatofflinexml.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.dam.fcojavier.chatofflinexml.dataAccess.ConversacionDAO;
import org.dam.fcojavier.chatofflinexml.dataAccess.UsuarioDAO;
import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.model.Mensaje;
import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.utils.SesionUsuario;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
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

    private UsuarioDAO usuarioDAO;
    private ConversacionDAO conversacionDAO;
    private Usuario usuarioLogueado;
    private String destinatarioActual;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Este método es llamado automáticamente por el FXMLLoader después de que los
     * campos @FXML han sido inyectados.
     */
    public void initialize() {
        this.usuarioDAO = new UsuarioDAO();
        this.conversacionDAO = new ConversacionDAO();
        this.usuarioLogueado = SesionUsuario.getInstance().getUsuarioActual();

        cargarUsuarios();
        configurarListeners();
    }

    /**
     * Carga todos los usuarios registrados en el ListView, excepto el usuario logueado.
     */
    private void cargarUsuarios() {
        userListView.getItems().setAll(
                usuarioDAO.getUsuariosLista().getUsuarios().stream()
                        .map(Usuario::getNombre)
                        .filter(nombre -> !nombre.equals(usuarioLogueado.getNombre()))
                        .collect(Collectors.toList())
        );
    }

    /**
     * Configura los manejadores de eventos para los controles de la interfaz.
     */
    private void configurarListeners() {
        userListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                destinatarioActual = newSelection;
                cargarConversacion(destinatarioActual);
            }
        });

        sendButton.setOnAction(event -> enviarMensaje());
        messageTextField.setOnAction(event -> enviarMensaje());
    }

    /**
     * Carga y muestra en la interfaz la conversación con el usuario seleccionado.
     * @param destinatario El nombre del usuario con quien se carga la conversación.
     */
    private void cargarConversacion(String destinatario) {
        chatVBox.getChildren().clear();

        Optional<Conversacion> convOpt = conversacionDAO.buscarConversacion(usuarioLogueado.getNombre(), destinatario);

        // Si la conversación existe Y NO está vacía, muestra los mensajes.
        if (convOpt.isPresent() && !convOpt.get().getMensajes().isEmpty()) {
            convOpt.get().getMensajes().forEach(this::addMensajeToView);
        } else {
            // Si no existe o está vacía, muestra el mensaje de bienvenida.
            mostrarMensajeBienvenida(destinatario);
        }

        // Auto-scroll hacia el último mensaje (si lo hay)
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    /**
     * Crea un objeto Mensaje y lo guarda usando el DAO.
     * Actualiza la interfaz para mostrar el mensaje enviado.
     */
    private void enviarMensaje() {
        String texto = messageTextField.getText();
        if (texto.isBlank() || destinatarioActual == null) {
            return;
        }

        // Si la vista de chat estaba mostrando el mensaje de bienvenida, la limpiamos antes de añadir el primer mensaje.
        if (chatVBox.getChildren().size() == 1 && chatVBox.getChildren().get(0).getStyleClass().contains("welcome-message")) {
            chatVBox.getChildren().clear();
        }

        // 1. Crear el objeto Mensaje
        Mensaje nuevoMensaje = new Mensaje(
                destinatarioActual,
                usuarioLogueado.getNombre(),
                texto,
                LocalDateTime.now(),
                null // Adjunto no implementado
        );

        // 2. Guardar el mensaje en el XML
        boolean guardado = conversacionDAO.guardarMensaje(nuevoMensaje, usuarioLogueado.getNombre(), destinatarioActual);

        // 3. Si se guardó, actualizar la interfaz
        if (guardado) {
            addMensajeToView(nuevoMensaje);
            messageTextField.clear();
            // Auto-scroll hacia el nuevo mensaje
            Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
        } else {
            // Opcional: Mostrar un error al usuario
            System.err.println("Error al guardar el mensaje.");
        }
    }

    /**
     * Añade una representación visual de un mensaje al VBox del chat.
     * @param mensaje El mensaje a mostrar.
     */
    private void addMensajeToView(Mensaje mensaje) {
        String formattedTime = mensaje.getFechaHora().format(formatter);
        String labelText = String.format("%s [%s]", mensaje.getContenido(), formattedTime);
        Label label = new Label(labelText);
        label.setWrapText(true);
        label.setMaxWidth(350); // Evita que el label ocupe todo el ancho

        // Contenedor para poder alinear el label
        HBox hbox = new HBox();
        hbox.getChildren().add(label);

        // Alinear a la derecha si es nuestro, a la izquierda si es del otro
        if (mensaje.getRemitente().equals(usuarioLogueado.getNombre())) {
            hbox.setAlignment(Pos.CENTER_RIGHT);
            label.setStyle("-fx-background-color: #dcf8c6; -fx-padding: 8; -fx-background-radius: 8;");
        } else {
            hbox.setAlignment(Pos.CENTER_LEFT);
            label.setStyle("-fx-background-color: #ffffff; -fx-padding: 8; -fx-background-radius: 8;");
        }

        chatVBox.getChildren().add(hbox);
        VBox.setMargin(hbox, new Insets(2, 0, 2, 0));
    }

    /**
     * Muestra un mensaje centrado para animar al usuario a iniciar la conversación.
     * @param destinatario El nombre del usuario con el que se va a chatear.
     */
    private void mostrarMensajeBienvenida(String destinatario) {
        Label label = new Label("¡Aún no hay mensajes! Sé el primero en saludar a " + destinatario + ".");
        label.setStyle("-fx-text-fill: grey; -fx-font-style: italic;");

        HBox hbox = new HBox(label);
        hbox.setAlignment(Pos.CENTER);

        hbox.getStyleClass().add("welcome-message");

        chatVBox.getChildren().add(hbox);
    }
}
