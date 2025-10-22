package org.dam.fcojavier.chatofflinexml.utils;

import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.model.Mensaje;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Clase de utilidad para analizar los datos de una conversación usando Java Streams.
 * Todos los métodos son estáticos y reciben el objeto Conversacion a analizar.
 */
public class AnalizadorConversacion {

    /**
     * Calcula el número total de mensajes en una conversación.
     * @param conversacion El objeto Conversacion a analizar.
     * @return El número total de mensajes.
     */
    public static int contarTotalMensajes(Conversacion conversacion) {
        if (conversacion == null || conversacion.getMensajes() == null) {
            return 0;
        }
        return conversacion.getMensajes().size();
    }

    /**
     * Cuenta cuántos mensajes ha enviado cada participante en la conversación.
     * @param conversacion El objeto Conversacion a analizar.
     * @return Un Map donde la clave es el nombre del remitente y el valor es el número de mensajes enviados.
     */
    public static Map<String, Long> contarMensajesPorUsuario(Conversacion conversacion) {
        if (conversacion == null || conversacion.getMensajes() == null) {
            return Collections.emptyMap(); // Devuelve un mapa vacío si no hay datos
        }

        return conversacion.getMensajes().stream()
                .collect(Collectors.groupingBy(
                        Mensaje::getRemitente,       // Agrupa por el remitente del mensaje
                        Collectors.counting()        // Cuenta las ocurrencias en cada grupo
                ));
    }

    /**
     * Analiza el contenido de todos los mensajes para encontrar las palabras más frecuentes.
     * @param conversacion La conversación a analizar.
     * @param limite El número de palabras a devolver (ej. 10 para el Top 10).
     * @return Un Map ordenado con las palabras más usadas y su frecuencia.
     */
    public static Map<String, Long> encontrarPalabrasMasUsadas(Conversacion conversacion, int limite) {
        if (conversacion == null || conversacion.getMensajes() == null) {
            return Collections.emptyMap();
        }

        return conversacion.getMensajes().stream() // 1. Stream de Mensajes
                .map(Mensaje::getContenido) // 2. Stream de Strings (contenido de cada mensaje)
                .filter(Objects::nonNull) // <-- FIX: Ignora mensajes sin contenido de texto
                .map(String::toLowerCase) // 3. Pasa todo a minúsculas
                .flatMap(linea -> Arrays.stream(linea.split("[\\s.,!?;:]+"))) // 4. Divide por espacios/puntuación y aplana a un stream de palabras
                .filter(palabra -> !palabra.isBlank()) // 5. Filtra posibles palabras vacías
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // 6. Agrupa por palabra y cuenta
                .entrySet().stream() // 7. Crea un nuevo stream de las entradas del mapa (palabra, frecuencia)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // 8. Ordena por frecuencia (de mayor a menor)
                .limit(limite) // 9. Se queda con el Top N
                .collect(Collectors.toMap( // 10. Lo recolecta en un nuevo mapa ordenado
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Importante usar LinkedHashMap para mantener el orden
                ));
    }
}
