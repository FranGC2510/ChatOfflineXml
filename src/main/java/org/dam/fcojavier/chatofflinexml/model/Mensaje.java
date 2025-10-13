package org.dam.fcojavier.chatofflinexml.model;

import java.time.LocalDateTime;

public class Mensaje {

    private Usuario destinatario;
    private Usuario remitente;
    private String contenido;
    private LocalDateTime fechaHora;
    private Adjunto adjunto;

    public Mensaje() {
    }
    public Mensaje(Usuario destinatario, Usuario remitente, String contenido, LocalDateTime fechaHora, Adjunto adjunto) {
        this.destinatario = destinatario;
        this.remitente = remitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.adjunto = adjunto;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public Usuario getRemitente() {
        return remitente;
    }

    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

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
