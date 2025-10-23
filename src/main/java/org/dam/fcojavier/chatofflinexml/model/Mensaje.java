package org.dam.fcojavier.chatofflinexml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dam.fcojavier.chatofflinexml.utils.AdaptadorLocalDateTimeXml;
import java.time.LocalDateTime;

/**
 * Clase que representa un mensaje en el sistema de chat offline.
 * Contiene información sobre el remitente, destinatario, contenido, fecha y hora, y un posible archivo adjunto.
 */
@XmlRootElement(name = "mensaje")
@XmlType(propOrder = { "remitente", "destinatario", "fechaHora", "contenido", "adjunto" })
public class Mensaje {

    private String destinatario;
    private String remitente;
    private String contenido;
    private LocalDateTime fechaHora;
    private Adjunto adjunto;

    /**
     * Constructor por defecto de la clase Mensaje.
     */
    public Mensaje() {
    }

    /**
     * Constructor para crear un nuevo mensaje con todos sus datos.
     * @param destinatario El correo electrónico del destinatario.
     * @param remitente El correo electrónico del remitente.
     * @param contenido El contenido del mensaje.
     * @param fechaHora La fecha y hora en que se envió el mensaje.
     * @param adjunto El archivo adjunto al mensaje (puede ser nulo).
     */
    public Mensaje(String destinatario, String remitente, String contenido, LocalDateTime fechaHora, Adjunto adjunto) {
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.adjunto = adjunto;
    }

    /**
     * Obtiene el correo electrónico del destinatario.
     * @return El correo electrónico del destinatario.
     */
    @XmlElement
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * Establece el correo electrónico del destinatario.
     * @param destinatario El nuevo correo electrónico del destinatario.
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Obtiene el correo electrónico del remitente.
     * @return El correo electrónico del remitente.
     */
    @XmlElement
    public String getRemitente() {
        return remitente;
    }

    /**
     * Establece el correo electrónico del remitente.
     * @param remitente El nuevo correo electrónico del remitente.
     */
    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    /**
     * Obtiene el contenido del mensaje.
     * @return El contenido del mensaje.
     */
    @XmlElement
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido del mensaje.
     * @param contenido El nuevo contenido del mensaje.
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene la fecha y hora en que se envió el mensaje.
     * @return La fecha y hora del mensaje.
     */
    @XmlElement
    @XmlJavaTypeAdapter(value = AdaptadorLocalDateTimeXml.class)
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora del mensaje.
     * @param fechaHora La nueva fecha y hora del mensaje.
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el archivo adjunto al mensaje.
     * @return El archivo adjunto al mensaje.
     */
    @XmlElement(name = "adjunto")
    public Adjunto getAdjunto() {
        return adjunto;
    }

    /**
     * Establece el archivo adjunto al mensaje.
     * @param adjunto El nuevo archivo adjunto al mensaje.
     */
    public void setAdjunto(Adjunto adjunto) {
        this.adjunto = adjunto;
    }

    /**
     * Devuelve una representación en cadena del objeto Mensaje.
     * @return Una cadena que contiene el destinatario, remitente, contenido, fecha y hora, y adjunto del mensaje.
     */
    @Override
    public String toString() {
        return "Mensaje{" + "destinatario=" + destinatario + ", remitente=" + remitente + ", contenido=" + contenido + ", fechaHora=" + fechaHora + ", adjunto=" + adjunto + '}';
    }
}
