package org.dam.fcojavier.chatofflinexml.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.utils.AnalizadorConversacion;

import java.util.Map;
import java.util.stream.Collectors;

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
     * Clase interna estática para representar los datos en la TableView.
     */
    public static class PalabraFrecuencia {
        private final String palabra;
        private final Long frecuencia;

        public PalabraFrecuencia(String palabra, Long frecuencia) {
            this.palabra = palabra;
            this.frecuencia = frecuencia;
        }

        public String getPalabra() {
            return palabra;
        }

        public Long getFrecuencia() {
            return frecuencia;
        }
    }

    /**
     * Método para inicializar el controlador con los datos de la conversación a analizar.
     * Este método debe ser llamado desde el ChatViewController después de cargar la vista.
     * @param conversacion La conversación a analizar.
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
     * Configura y puebla la TableView con las 10 palabras más usadas.
     * @param conversacion La conversación a analizar.
     */
    private void configurarTablaPalabras(Conversacion conversacion) {
        // Configurar las celdas de la tabla
        palabraColumn.setCellValueFactory(new PropertyValueFactory<>("palabra"));
        frecuenciaColumn.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));

        // Obtener los datos del analizador
        Map<String, Long> palabrasMasUsadas = AnalizadorConversacion.encontrarPalabrasMasUsadas(conversacion, 10);

        // Convertir el mapa a una lista observable de nuestro record
        ObservableList<PalabraFrecuencia> datosTabla = palabrasMasUsadas.entrySet().stream()
                .map(entry -> new PalabraFrecuencia(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        palabrasTableView.setItems(datosTabla);
    }

    /**
     * Configura y puebla el PieChart con la participación de cada usuario.
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
