package org.dam.fcojavier.chatofflinexml.model;


import javax.xml.bind.annotation.*;
import java.util.HashSet;


@XmlRootElement(name = "Usuarios")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UsuariosLista {
    /**
     * Conjunto de usuarios almacenados en la lista.
     */
    private HashSet<Usuario> usuarios = new HashSet<>();

    /**
     * Constructor vacío requerido por JAXB para la deserialización.
     */
    public UsuariosLista() {}

    /**
     * Obtiene la lista de usuarios almacenados.
     * @return Un conjunto de objetos de tipo {@link Usuario}.
     */
    @XmlElement(name = "usuario")
    public HashSet<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
     * Establece la lista de usuarios almacenados.
     * @param usuarios Conjunto de usuarios que se asignará a la lista.
     */
    public void setUsuarios(HashSet<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Agrega un usuario a la lista si no está presente.
     * @param usuario El usuario que se desea agregar.
     * @return true si el usuario fue agregado con éxito, false si ya estaba en la lista.
     */
    public boolean addUsuario(Usuario usuario) {
        return this.usuarios.add(usuario);
    }

    /**
     * Elimina un usuario de la lista.
     * @param usuario El usuario a eliminar.
     * @return true si el usuario fue eliminado, false si no existía.
     */
    public boolean removeUsuario(Usuario usuario) {
        return this.usuarios.remove(usuario);
    }

    /**
     * Devuelve una representación en cadena de la lista de usuarios.
     * @return Una cadena con la información de los usuarios almacenados.
     */
    public String toString() {
        return usuarios.toString();
    }
}
