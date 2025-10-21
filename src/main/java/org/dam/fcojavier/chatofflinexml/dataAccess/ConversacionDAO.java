package org.dam.fcojavier.chatofflinexml.dataAccess;

import org.dam.fcojavier.chatofflinexml.model.Conversacion;
import org.dam.fcojavier.chatofflinexml.model.Mensaje;
import org.dam.fcojavier.chatofflinexml.utils.XmlManager;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

/**
 * Clase DAO para gestionar la persistencia de conversaciones.
 * Cada conversación se guarda en su propio archivo XML para mayor eficiencia.
 */
public class ConversacionDAO {
    private static final String CONVERSACIONES_DIR = "src/main/resources/data/conversaciones/";

    /**
     * Constructor vacío requerido por JAXB para la deserialización.
     */
    public ConversacionDAO() {
    }

    /**
     * Genera la ruta del archivo para una conversación entre dos usuarios.
     * Ordena los nombres de usuario para asegurar una única ruta por par de usuarios.
     * @param usuario1 Un participante.
     * @param usuario2 El otro participante.
     * @return La ruta absoluta al archivo XML de la conversación.
     */
    private String getConversationPath(String usuario1, String usuario2) {
        String[] usuarios = {usuario1, usuario2};
        Arrays.sort(usuarios);
        String fileName = usuarios[0] + "_" + usuarios[1] + ".xml";
        return Paths.get(CONVERSACIONES_DIR, fileName).toString();
    }

    /**
     * Guarda un objeto Conversacion en su archivo XML correspondiente.
     * @param conversacion La conversación a guardar.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    private boolean guardarConversacion(Conversacion conversacion) {
        String path = getConversationPath(conversacion.getUsuario1(), conversacion.getUsuario2());
        return XmlManager.writeXML(conversacion, path);
    }

    /**
     * Busca y carga una conversación existente entre dos usuarios desde su archivo XML.
     * @param usuario1 Un participante.
     * @param usuario2 El otro participante.
     * @return Un Optional<Conversacion> si el archivo existe y se puede leer.
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
     * Guarda un nuevo mensaje.
     * Carga la conversación si existe, o crea una nueva. Añade el mensaje y guarda.
     * @param mensaje El objeto Mensaje a guardar.
     * @param remitente El username del remitente.
     * @param destinatario El username del destinatario.
     * @return true si se pudo guardar, false en caso de error.
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
