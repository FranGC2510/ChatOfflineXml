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
 * Permite a los nuevos usuarios registrarse en el sistema proporcionando sus datos personales y una contraseña.
 * Realiza validaciones de los campos y, si el registro es exitoso, inicia sesión automáticamente al usuario.
 */
public class RegistroController {
    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellido;

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoPassword;

    @FXML
    private PasswordField campoConfirmarPassword;

    private UsuarioDAO usuarioDAO;
    private SesionUsuario gestorSesion;

    /**
     * Inicializa el controlador después de que se hayan cargado los elementos FXML.
     * Se encarga de instanciar los objetos necesarios para la gestión de usuarios y la sesión.
     */
    @FXML
    public void inicializar() {
        usuarioDAO = new UsuarioDAO();
        gestorSesion = SesionUsuario.getInstance();
    }

    /**
     * Maneja el evento de registro de un nuevo usuario.
     * Recoge los datos de los campos de texto, realiza validaciones (campos vacíos, formato de email, formato de contraseña,
     * coincidencia de contraseñas, email existente) y, si todo es correcto, registra al usuario.
     * Si el registro es exitoso, el usuario inicia sesión y se abre la ventana de chat.
     */
    @FXML
    private void gestionarRegistro() {
        String nombre = campoNombre.getText();
        String apellido = campoApellido.getText();
        String email = campoEmail.getText();
        String password = campoPassword.getText();
        String confirmarPassword = campoConfirmarPassword.getText();

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
            gestorSesion.setUsuarioActual(nuevoUsuario);

            // 2. Abrir la ventana de chat
            abrirVentanaChat();
        } else {
            mostrarAlerta("Error", "Error al registrar el usuario", Alert.AlertType.ERROR);
        }
    }

    /**
     * Carga y muestra la ventana principal del chat y cierra la ventana de registro actual.
     * En caso de error al cargar la ventana, muestra una alerta.
     */
    private void abrirVentanaChat() {
        try {
            // Cargar el FXML de la vista de chat
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/ChatView.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena y un nuevo stage
            Stage stage = new Stage();
            stage.setTitle("Chat Offline - " + gestorSesion.getUsuarioActual().getNombre());
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar la ventana de registro actual
            Stage registroStage = (Stage) campoNombre.getScene().getWindow();
            registroStage.close();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de chat.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de cancelar el registro.
     * Cierra la ventana de registro actual.
     */
    @FXML
    private void gestionarCancelacion() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual del registro.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) campoNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta con el título, mensaje y tipo especificados.
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar en la alerta.
     * @param tipo El tipo de alerta (ERROR, INFORMATION, WARNING, etc.).
     */
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
