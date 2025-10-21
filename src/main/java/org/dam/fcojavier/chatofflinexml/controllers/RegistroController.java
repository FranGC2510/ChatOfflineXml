package org.dam.fcojavier.chatofflinexml.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.dam.fcojavier.chatofflinexml.dataAccess.UsuarioDAO;
import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.utils.PasswordUtilidades;
import org.dam.fcojavier.chatofflinexml.utils.SesionUsuario;
import org.dam.fcojavier.chatofflinexml.utils.Validacion;

import java.io.IOException;

/**
 * Controlador para la vista de registro de usuarios.
 */
public class RegistroController {
    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmarPassword;

    private UsuarioDAO usuarioDAO;
    private SesionUsuario sessionManager;

    /**
     * Inicializa el controlador.
     */
    @FXML
    public void initialize() {
        usuarioDAO = new UsuarioDAO();
        sessionManager = SesionUsuario.getInstance();
    }

    /**
     * Maneja el evento de registro de un nuevo usuario.
     */
    @FXML
    private void handleRegistrar() {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String confirmarPassword = txtConfirmarPassword.getText();

        // Validaciones de campos vacíos
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarAlerta("Error", "El nombre no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            mostrarAlerta("Error", "El apellido no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            mostrarAlerta("Error", "El email no puede estar vacío", Alert.AlertType.ERROR);
            return;
        }

        // Validación de formato de email usando Validacion
        if (!Validacion.isValidoEmail(email)) {
            mostrarAlerta("Error", Validacion.ERROR_EMAIL, Alert.AlertType.ERROR);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            mostrarAlerta("Error", "La contraseña no puede estar vacía", Alert.AlertType.ERROR);
            return;
        }

        // Validación de formato de contraseña usando Validacion
        if (!Validacion.isValidaPassword(password)) {
            mostrarAlerta("Error", Validacion.ERROR_PASSWORD, Alert.AlertType.ERROR);
            return;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmarPassword)) {
            mostrarAlerta("Error", "Las contraseñas no coinciden", Alert.AlertType.ERROR);
            return;
        }

        // Verificar si el email ya existe
        if (usuarioDAO.existeEmail(email)) {
            mostrarAlerta("Error", "Ya existe un usuario con ese email", Alert.AlertType.ERROR);
            return;
        }

        // Hashear la contraseña antes de guardar
        String passwordHasheada = PasswordUtilidades.hashPassword(password);

        // Crear y registrar el usuario
        Usuario nuevoUsuario = new Usuario(nombre.trim(), apellido.trim(), email.trim().toLowerCase(), passwordHasheada);
        boolean registrado = usuarioDAO.registrarUsuario(nuevoUsuario);

        if (registrado) {
            // 1. Establecer la sesión para el nuevo usuario
            sessionManager.setUsuarioActual(nuevoUsuario);

            // 2. Abrir la ventana de chat
            abrirVentanaChat();
        } else {
            mostrarAlerta("Error", "Error al registrar el usuario", Alert.AlertType.ERROR);
        }
    }

    /**
     * Carga y muestra la ventana principal del chat y cierra la ventana de registro.
     */
    private void abrirVentanaChat() {
        try {
            // Cargar el FXML de la vista de chat
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/ChatView.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena y un nuevo stage
            Stage stage = new Stage();
            stage.setTitle("Chat Offline - " + sessionManager.getUsuarioActual().getNombre());
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana de registro actual
            Stage registroStage = (Stage) txtNombre.getScene().getWindow();
            registroStage.close();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de chat.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de cancelar el registro.
     */
    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     * @param titulo Título de la alerta.
     * @param mensaje Mensaje de la alerta.
     * @param tipo Tipo de alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
