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

public class LoginController {
    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

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
     * Maneja el evento de inicio de sesión.
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
                mostrarAlerta("Éxito", "Bienvenido " + usuario.getNombre(), Alert.AlertType.INFORMATION);
                // Aquí podrías abrir la ventana principal de la aplicación
                // abrirVentanaPrincipal();
            } else {
                mostrarAlerta("Error", "Email o contraseña incorrectos", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Error", "Email o contraseña incorrectos", Alert.AlertType.ERROR);
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