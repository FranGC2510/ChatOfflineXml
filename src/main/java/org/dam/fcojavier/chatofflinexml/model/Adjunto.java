package org.dam.fcojavier.chatofflinexml.model;

import java.util.Set;

/**
 * Clase que representa un archivo adjunto en un mensaje.
 * Contiene información sobre el nombre, tipo, ruta y tamaño del archivo.
 * También define constantes para el tamaño máximo permitido y las extensiones de archivo válidas.
 */
public class Adjunto {
    /**
     * Tamaño máximo permitido para un archivo adjunto (10 MB).
     */
    public static final long TAMANO_MAXIMO = 10 * 1024 * 1024; // 10 MB
    /**
     * Conjunto de extensiones de archivo permitidas para los adjuntos.
     */
    public static final Set<String> EXTENSIONES_PERMITIDAS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp",  // Imágenes
            "pdf", "doc", "docx", "txt",                  // Documentos
            "zip", "rar"                                   // Comprimidos
    );

    private String nombre;
    private String tipo;
    private String ruta;
    private long tamano;

    /**
     * Constructor por defecto de la clase Adjunto.
     */
    public Adjunto() {
    }
    /**
     * Constructor para crear un nuevo adjunto con todos sus datos.
     * @param nombre El nombre del archivo adjunto.
     * @param tipo El tipo del archivo adjunto.
     * @param ruta La ruta donde se almacena el archivo adjunto.
     * @param tamano El tamaño del archivo adjunto en bytes.
     */
    public Adjunto(String nombre, String tipo, String ruta, long tamano) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ruta = ruta;
        this.tamano = tamano;
    }

    /**
     * Obtiene el nombre del archivo adjunto.
     * @return El nombre del archivo.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del archivo adjunto.
     * @param nombre El nuevo nombre del archivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el tipo del archivo adjunto.
     * @return El tipo del archivo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo del archivo adjunto.
     * @param tipo El nuevo tipo del archivo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la ruta donde se almacena el archivo adjunto.
     * @return La ruta del archivo.
     */
    public String getRuta() {
        return ruta;
    }

    /**
     * Establece la ruta donde se almacena el archivo adjunto.
     * @param ruta La nueva ruta del archivo.
     */
    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Obtiene el tamaño del archivo adjunto en bytes.
     * @return El tamaño del archivo.
     */
    public long getTamano() {
        return tamano;
    }

    /**
     * Establece el tamaño del archivo adjunto en bytes.
     * @param tamano El nuevo tamaño del archivo.
     */
    public void setTamano(long tamano) {
        this.tamano = tamano;
    }

    /**
     * Obtiene la extensión del archivo a partir de su nombre.
     * @return La extensión del archivo en minúsculas, o una cadena vacía si no tiene extensión.
     */
    public String getExtension() {
        if (nombre == null || !nombre.contains(".")) {
            return "";
        }
        return nombre.substring(nombre.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Verifica si el archivo adjunto es una imagen.
     * @return true si la extensión corresponde a un formato de imagen permitido, false en caso contrario.
     */
    public boolean esImagen() {
        String ext = getExtension();
        return ext.equals("jpg") || ext.equals("jpeg") ||
                ext.equals("png") || ext.equals("gif") ||
                ext.equals("bmp") || ext.equals("webp");
    }

    /**
     * Verifica si el archivo adjunto es un documento PDF.
     * @return true si la extensión es "pdf", false en caso contrario.
     */
    public boolean esPdf() {
        return getExtension().equals("pdf");
    }

    /**
     * Valida si el adjunto cumple con los requisitos de tamaño y extensión.
     * @return true si el tamaño es menor o igual al máximo permitido y la extensión está en la lista de permitidas, false en caso contrario.
     */
    public boolean esValido() {
        if (tamano > TAMANO_MAXIMO) {
            return false;
        }
        String extension = getExtension();
        return EXTENSIONES_PERMITIDAS.contains(extension);
    }

    /**
     * Devuelve una representación en cadena del objeto Adjunto.
     * @return Una cadena que contiene el nombre, tipo, ruta y tamaño del adjunto.
     */
    @Override
    public String toString() {
        return "Adjunto{" + "nombre=" + nombre + ", tipo=" + tipo + ", ruta=" + ruta + ", tamano=" + tamano + '}';
    }
}
