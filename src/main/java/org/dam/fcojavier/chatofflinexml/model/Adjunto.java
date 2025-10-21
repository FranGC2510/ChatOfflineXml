package org.dam.fcojavier.chatofflinexml.model;

import java.util.Set;

public class Adjunto {
    public static final long TAMANO_MAXIMO = 10 * 1024 * 1024; // 10 MB
    public static final Set<String> EXTENSIONES_PERMITIDAS = Set.of(
            "jpg", "jpeg", "png", "gif", "bmp", "webp",  // Imágenes
            "pdf", "doc", "docx", "txt",                  // Documentos
            "zip", "rar"                                   // Comprimidos
    );

    private String nombre;
    private String tipo;
    private String ruta;
    private long tamano;

    public Adjunto() {
    }
    public Adjunto(String nombre, String tipo, String ruta, long tamano) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.ruta = ruta;
        this.tamano = tamano;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public long getTamano() {
        return tamano;
    }

    public void setTamano(long tamano) {
        this.tamano = tamano;
    }

    /**
     * Obtiene la extensión del archivo
     */
    public String getExtension() {
        if (nombre == null || !nombre.contains(".")) {
            return "";
        }
        return nombre.substring(nombre.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Verifica si el adjunto es una imagen
     */
    public boolean esImagen() {
        String ext = getExtension();
        return ext.equals("jpg") || ext.equals("jpeg") ||
                ext.equals("png") || ext.equals("gif") ||
                ext.equals("bmp") || ext.equals("webp");
    }

    /**
     * Verifica si el adjunto es un PDF
     */
    public boolean esPdf() {
        return getExtension().equals("pdf");
    }

    /**
     * Valida si el adjunto cumple con los requisitos (tamaño y extensión)
     * @return true si es válido, false en caso contrario
     */
    public boolean esValido() {
        if (tamano > TAMANO_MAXIMO) {
            return false;
        }
        String extension = getExtension();
        return EXTENSIONES_PERMITIDAS.contains(extension);
    }

    @Override
    public String toString() {
        return "Adjunto{" + "nombre=" + nombre + ", tipo=" + tipo + ", ruta=" + ruta + ", tamano=" + tamano + '}';
    }
}
