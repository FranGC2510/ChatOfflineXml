package org.dam.fcojavier.chatofflinexml.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.dam.fcojavier.chatofflinexml.utils.AdaptadorLocalDateTimeXml;
import java.time.LocalDateTime;

@XmlRootElement(name = "mensaje")
@XmlType(propOrder = { "remitente", "destinatario", "fechaHora", "contenido" })
public class Mensaje {

    private String destinatario;
    private String remitente;
    private String contenido;
    private LocalDateTime fechaHora;
    private Adjunto adjunto;

    public Mensaje() {
    }
    public Mensaje(String destinatario, String remitente, String contenido, LocalDateTime fechaHora, Adjunto adjunto) {
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.adjunto = adjunto;
    }

    @XmlElement
    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    @XmlElement
    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    @XmlElement
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @XmlElement
    @XmlJavaTypeAdapter(value = AdaptadorLocalDateTimeXml.class)
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    // Se marca como @XmlTransient para que JAXB ignore esta propiedad,
    // ya que no está incluida en propOrder y aún no se ha implementado.
    @XmlTransient
    public Adjunto getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(Adjunto adjunto) {
        this.adjunto = adjunto;
    }

    @Override
    public String toString() {
        return "Mensaje{" + "destinatario=" + destinatario + ", remitente=" + remitente + ", contenido=" + contenido + ", fechaHora=" + fechaHora + ", adjunto=" + adjunto + '}';
    }
}
