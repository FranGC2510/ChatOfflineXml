package org.dam.fcojavier.chatofflinexml.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.utils.AnalizadorConversacion;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para la ventana de estadísticas de una conversación.
 * Muestra información como el número total de mensajes, las palabras más frecuentes y la participación de cada usuario.
 */
public class EstadisticasController {

    // --- FXML Fields ---
    @FXML
    private Label totalMensajesLabel;

    @FXML
    private TableView<PalabraFrecuencia> palabrasTableView;

    @FXML
    private TableColumn<PalabraFrecuencia, String> palabraColumn;

    @FXML
    private TableColumn<PalabraFrecuencia, Long> frecuenciaColumn;

    @FXML
    private PieChart participacionPieChart;

    /**
     * Clase interna para representar la frecuencia de las palabras en la TableView.
     * @param palabra La palabra encontrada en la conversación.
     * @param frecuencia El número de veces que la palabra aparece.
     */
    public record PalabraFrecuencia(String palabra, Long frecuencia) {
    }

    /**
     * Inicializa el controlador con los datos de la conversación a analizar.
     * Este método debe ser llamado desde el ChatViewController después de cargar la vista.
     * @param conversacion La conversación de la cual se extraerán y mostrarán las estadísticas.
     */
    public void initData(Conversacion conversacion) {
        if (conversacion == null) {
            return;
        }

        // 1. Calcular y mostrar el total de mensajes
        int totalMensajes = AnalizadorConversacion.contarTotalMensajes(conversacion);
        totalMensajesLabel.setText("Total de mensajes: " + totalMensajes);

        // 2. Calcular y mostrar las palabras más usadas
        configurarTablaPalabras(conversacion);

        // 3. Calcular y mostrar la participación por usuario
        configurarGraficoParticipacion(conversacion);
    }

    /**
     * Configura y puebla la TableView con las 10 palabras más usadas en la conversación.
     * @param conversacion La conversación a analizar.
     */
    private void configurarTablaPalabras(Conversacion conversacion) {
        // Configurar las celdas de la tabla para que sean compatibles con el 'record'
        palabraColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().palabra()));
        frecuenciaColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().frecuencia()));

        // Obtener los datos del analizador
        Map<String, Long> palabrasMasUsadas = AnalizadorConversacion.encontrarPalabrasMasUsadas(conversacion, 10);

        // Convertir el mapa a una lista observable para la TableView
        ObservableList<PalabraFrecuencia> datosTabla = palabrasMasUsadas.entrySet().stream()
                .map(entry -> new PalabraFrecuencia(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        palabrasTableView.setItems(datosTabla);
    }

    /**
     * Configura y puebla el PieChart con la participación de cada usuario en la conversación.
     * La participación se mide por el número de mensajes enviados por cada usuario.
     * @param conversacion La conversación a analizar.
     */
    private void configurarGraficoParticipacion(Conversacion conversacion) {
        Map<String, Long> mensajesPorUsuario = AnalizadorConversacion.contarMensajesPorUsuario(conversacion);

        ObservableList<PieChart.Data> pieChartData = mensajesPorUsuario.entrySet().stream()
                .map(entry -> new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        participacionPieChart.setData(pieChartData);
    }
}
