package org.dam.fcojavier.chatofflinexml.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversacion {
    private String usuario1;
    private String usuario2;
    private List<Mensaje> mensajes;

    public Conversacion() {
    }

    public Conversacion(String usuario1, String usuario2) {
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
        this.mensajes = new ArrayList<>();
    }

    public String getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public void addMensaje(Mensaje mensaje){
        this.mensajes.add(mensaje);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Conversacion that = (Conversacion) o;
        return Objects.equals(usuario1, that.usuario1) && Objects.equals(usuario2, that.usuario2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario1, usuario2);
    }

    @Override
    public String toString() {
        return "Conversacion{" +
                "usuario1=" + usuario1 +
                ", usuario2=" + usuario2 +
                ", mensajes=" + mensajes +
                '}';
    }
}
