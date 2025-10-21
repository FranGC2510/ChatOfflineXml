package org.dam.fcojavier.chatofflinexml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class ChatOfflineApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/login-view.fxml"));
        Parent root = loader.load();

        // Configurar la escena
        Scene scene = new Scene(root);

        // --- Carga del Icono ---
        // Intentamos establecer el icono de la ventana. En macOS, esto no afectará al Dock, pero es inofensivo.
        try {
            Image appIcon = new Image("/images/logo2.png");
            primaryStage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo cargar el icono de la aplicación desde /images/logo2.png. El icono no se mostrará.");
        }

        // Configurar el stage
        primaryStage.setTitle("Chat Offline - Inicio de Sesión");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
