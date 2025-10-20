package org.dam.fcojavier.chatofflinexml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatOfflineApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML de login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/dam/fcojavier/chatofflinexml/login-view.fxml"));
        Parent root = loader.load();

        // Configurar la escena
        Scene scene = new Scene(root);

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
