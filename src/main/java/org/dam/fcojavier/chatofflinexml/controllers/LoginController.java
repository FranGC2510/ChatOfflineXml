package org.dam.fcojavier.chatofflinexml.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.dam.fcojavier.chatofflinexml.dataAccess.UsuarioDAO;
import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.utils.PasswordUtilidades;
import org.dam.fcojavier.chatofflinexml.utils.SesionUsuario;
import org.dam.fcojavier.chatofflinexml.utils.Validacion;

import java.io.IOException;
import java.util.Optional;

/**
 * Controlador para la ventana de inicio de sesión.
 * Permite a los usuarios ingresar con su email y contraseña para acceder a la aplicación de chat.
 */
public class LoginController {
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    private UsuarioDAO usuarioDAO;
    private SesionUsuario sessionManager;

    /**
     * Inicializa el controlador.
     * Se encarga de instanciar los objetos necesarios para la gestión de usuarios y la sesión.
     */
    @FXML
    public void initialize() {
        usuarioDAO = new UsuarioDAO();
        sessionManager = SesionUsuario.getInstance();
    }

    /**
     * Maneja el evento de inicio de sesión.
     * Valida las credenciales del usuario y, si son correctas, abre la ventana de chat.
     */
    @FXML
    private void handleIniciarSesion() {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        // Validaciones de campos vacíos
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

        // Buscar usuario por email
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email.trim().toLowerCase());

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Verificar la contraseña usando BCrypt
            if (PasswordUtilidades.checkPassword(password, usuario.getPassword())) {
                sessionManager.setUsuarioActual(usuario);
                abrirVentanaChat();
            } else {
                mostrarAlerta("Error", "Email o contraseña incorrectos", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Error", "Email o contraseña incorrectos", Alert.AlertType.ERROR);
        }
    }

    /**
     * Carga y muestra la ventana principal del chat y cierra la ventana de login.
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

            // Cerrar la ventana de login actual
            Stage loginStage = (Stage) txtEmail.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de chat.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    /**
     * Maneja el evento de abrir la ventana de registro.
     */
    @FXML
    private void handleAbrirRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/registro-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registro de Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de registro", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
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
