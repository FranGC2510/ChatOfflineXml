package org.dam.fcojavier.chatofflinexml.model;


import javax.xml.bind.annotation.*;
import java.util.HashSet;

/**
 * Clase que representa una lista de usuarios.
 * Utiliza un HashSet para almacenar los usuarios y garantizar que no haya duplicados.
 */
@XmlRootElement(name = "Usuarios")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UsuariosLista {

    private HashSet<Usuario> usuarios = new HashSet<>();

    /**
     * Constructor por defecto de la clase UsuariosLista.
     */
    public UsuariosLista() {}

    /**
     * Obtiene el conjunto de usuarios.
     * @return Un HashSet que contiene los usuarios.
     */
    @XmlElement(name = "usuario")
    public HashSet<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Establece el conjunto de usuarios.
     * @param usuarios El nuevo conjunto de usuarios.
     */
    public void setUsuarios(HashSet<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Agrega un usuario a la lista.
     * @param usuario El usuario a agregar.
     * @return true si el usuario se agregó correctamente, false si ya existía.
     */
    public boolean addUsuario(Usuario usuario) {
        return this.usuarios.add(usuario);
    }

    /**
     * Elimina un usuario de la lista.
     * @param usuario El usuario a eliminar.
     * @return true si el usuario se eliminó correctamente, false si no existía.
     */
    public boolean removeUsuario(Usuario usuario) {
        return this.usuarios.remove(usuario);
    }

    /**
     * Devuelve una representación en cadena de la lista de usuarios.
     * @return Una cadena que representa el conjunto de usuarios.
     */
    public String toString() {
        return usuarios.toString();
    }
}
