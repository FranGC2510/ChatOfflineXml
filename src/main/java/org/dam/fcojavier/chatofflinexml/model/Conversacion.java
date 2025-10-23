package org.dam.fcojavier.chatofflinexml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa una conversación entre dos usuarios.
 * Contiene los nombres de los dos usuarios y una lista de los mensajes intercambiados.
 */
@XmlRootElement(name = "conversacion")
// Se añade esta anotación para definir el orden de los elementos en el XML
@XmlType(propOrder = { "usuario1", "usuario2", "mensajes" })
public class Conversacion {

    private String usuario1;
    private String usuario2;
    private List<Mensaje> mensajes;

    /**
     * Constructor por defecto de la clase Conversacion.
     * Inicializa la lista de mensajes.
     */
    public Conversacion() {
        this.mensajes = new ArrayList<>();
    }

    /**
     * Constructor para crear una nueva conversación entre dos usuarios.
     * @param usuario1 El nombre del primer usuario.
     * @param usuario2 El nombre del segundo usuario.
     */
    public Conversacion(String usuario1, String usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.mensajes = new ArrayList<>();
    }

    /**
     * Obtiene el nombre del primer usuario.
     * @return El nombre del primer usuario.
     */
    @XmlElement
    public String getUsuario1() {
        return usuario1;
    }

    /**
     * Establece el nombre del primer usuario.
     * @param usuario1 El nuevo nombre del primer usuario.
     */
    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    /**
     * Obtiene el nombre del segundo usuario.
     * @return El nombre del segundo usuario.
     */
    @XmlElement
    public String getUsuario2() {
        return usuario2;
    }

    /**
     * Establece el nombre del segundo usuario.
     * @param usuario2 El nuevo cnombre del segundo usuario.
     */
    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    /**
     * Obtiene la lista de mensajes de la conversación.
     * @return La lista de mensajes.
     */
    @XmlElementWrapper(name = "mensajes")
    @XmlElement(name = "mensaje")
    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    /**
     * Establece la lista de mensajes de la conversación.
     * @param mensajes La nueva lista de mensajes.
     */
    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    /**
     * Agrega un mensaje a la conversación.
     * @param mensaje El mensaje a agregar.
     */
    public void addMensaje(Mensaje mensaje){
        this.mensajes.add(mensaje);
    }

    /**
     * Compara este objeto Conversacion con el objeto especificado.
     * La comparación se basa en los nombres de los dos usuarios, sin importar el orden.
     * @param o El objeto a comparar.
     * @return true si los objetos son iguales (tienen los mismos usuarios), false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversacion that = (Conversacion) o;
        return (Objects.equals(usuario1, that.usuario1) && Objects.equals(usuario2, that.usuario2)) ||
               (Objects.equals(usuario1, that.usuario2) && Objects.equals(usuario2, that.usuario1));
    }

    /**
     * Devuelve un valor de código hash para este objeto.
     * El código hash se basa en los nombres de los dos usuarios y es independiente del orden.
     * @return Un valor de código hash para este objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(usuario1) + Objects.hashCode(usuario2);
    }

    /**
     * Devuelve una representación en cadena del objeto Conversacion.
     * @return Una cadena que contiene los usuarios y los mensajes de la conversación.
     */
    @Override
    public String toString() {
        return "Conversacion{" +
                "usuario1=" + usuario1 +
                ", usuario2=" + usuario2 +
                ", mensajes=" + mensajes +
                '}';
    }
}
