package org.dam.fcojavier.chatofflinexml.dataAccess;

import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.model.Mensaje;
import org.dam.fcojavier.chatofflinexml.utils.XmlManager;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

/**
 * Clase DAO (Data Access Object) para gestionar la persistencia de conversaciones.
 * Se encarga de leer, guardar y actualizar las conversaciones en archivos XML individuales.
 * Cada conversación se identifica por los nombres de los dos usuarios participantes.
 */
public class ConversacionDAO {
    /**
     * Directorio donde se almacenan los archivos XML de las conversaciones.
     */
    private static final String CONVERSACIONES_DIR = "src/main/resources/data/conversaciones/";

    /**
     * Constructor por defecto de la clase ConversacionDAO.
     */
    public ConversacionDAO() {
    }

    /**
     * Genera la ruta completa del archivo XML para una conversación específica entre dos usuarios.
     * Los nombres de los usuarios se ordenan alfabéticamente para asegurar un nombre de archivo consistente
     * y único para cada par de usuarios, independientemente del orden en que se proporcionen.
     * @param usuario1 El nombre del primer participante en la conversación.
     * @param usuario2 El nombre del segundo participante en la conversación.
     * @return La ruta absoluta al archivo XML donde se guarda o se leerá la conversación.
     */
    private String getConversationPath(String usuario1, String usuario2) {
        String[] usuarios = {usuario1, usuario2};
        Arrays.sort(usuarios);
        String fileName = usuarios[0] + "_" + usuarios[1] + ".xml";
        return Paths.get(CONVERSACIONES_DIR, fileName).toString();
    }

    /**
     * Guarda un objeto {@link Conversacion} en su archivo XML correspondiente.
     * La ruta del archivo se determina usando los nombres de los usuarios de la conversación.
     * @param conversacion La instancia de {@link Conversacion} que se desea guardar.
     * @return {@code true} si la conversación se guardó exitosamente, {@code false} en caso contrario.
     */
    private boolean guardarConversacion(Conversacion conversacion) {
        String path = getConversationPath(conversacion.getUsuario1(), conversacion.getUsuario2());
        return XmlManager.writeXML(conversacion, path);
    }

    /**
     * Busca y carga una conversación existente entre dos usuarios desde su archivo XML.
     * Si no se encuentra el archivo o no se puede leer, devuelve un {@link Optional#empty()}.
     * @param usuario1 El nombre del primer participante.
     * @param usuario2 El nombre del segundo participante.
     * @return Un {@link Optional} que contiene la {@link Conversacion} si se encuentra y se puede cargar,
     *         o un {@link Optional#empty()} si la conversación no existe o hay un error de lectura.
     */
    public Optional<Conversacion> buscarConversacion(String usuario1, String usuario2) {
        String path = getConversationPath(usuario1, usuario2);
        File file = new File(path);

        if (!file.exists()) {
            return Optional.empty();
        }

        Conversacion conversacion = XmlManager.readXML(new Conversacion(), path);
        return Optional.ofNullable(conversacion);
    }

    /**
     * Guarda un nuevo mensaje en la conversación correspondiente entre el remitente y el destinatario.
     * Si la conversación no existe previamente, se crea una nueva. El mensaje se añade a la lista de mensajes
     * de la conversación y luego la conversación (actualizada o nueva) se guarda en su archivo XML.
     * @param mensaje El objeto {@link Mensaje} que se desea guardar.
     * @param remitente El nombre del remitente del mensaje.
     * @param destinatario El nombre del destinatario del mensaje.
     * @return {@code true} si el mensaje se guardó exitosamente dentro de la conversación, {@code false} en caso de error.
     */
    public boolean guardarMensaje(Mensaje mensaje, String remitente, String destinatario) {
        try {
            // Busca la conversación o crea una nueva si no existe.
            Conversacion conversacion = buscarConversacion(remitente, destinatario)
                    .orElse(new Conversacion(remitente, destinatario));

            // Añade el nuevo mensaje al historial.
            conversacion.getMensajes().add(mensaje);

            // Guarda la conversación (actualizada o nueva) en su archivo.
            return guardarConversacion(conversacion);

        } catch (Exception e) {
            System.err.println("Error al guardar el mensaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
