package org.dam.fcojavier.chatofflinexml.dataAccess;

import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.model.UsuariosLista;
import org.dam.fcojavier.chatofflinexml.utils.XmlManager;

import java.util.Optional;

/**
 * Clase DAO para gestionar las operaciones de persistencia de usuarios.
 */
public class UsuarioDAO {
    private static final String USUARIOS_XML = "src/main/resources/data/usuarios.xml";
    private UsuariosLista usuariosLista;

    /**
     * Constructor que carga la lista de usuarios desde el archivo XML.
     */
    public UsuarioDAO() {
        cargarUsuarios();
    }

    /**
     * Carga la lista de usuarios desde el archivo XML.
     */
    private void cargarUsuarios() {
        usuariosLista = XmlManager.readXML(new UsuariosLista(), USUARIOS_XML);
        if (usuariosLista == null) {
            usuariosLista = new UsuariosLista();
        }
    }

    /**
     * Guarda la lista de usuarios en el archivo XML.
     * @return true si se guardó correctamente, false en caso contrario.
     */
    private boolean guardarUsuarios() {
        return XmlManager.writeXML(usuariosLista, USUARIOS_XML);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * @param usuario El usuario a registrar.
     * @return true si el registro fue exitoso, false si el usuario ya existe.
     */
    public boolean registrarUsuario(Usuario usuario) {
        if (existeEmail(usuario.getEmail())) {
            return false;
        }
        boolean agregado = usuariosLista.addUsuario(usuario);
        if (agregado) {
            guardarUsuarios();
        }
        return agregado;
    }

    /**
     * Verifica si existe un usuario con el email especificado.
     * @param email El email a buscar.
     * @return true si existe un usuario con ese email, false en caso contrario.
     */
    public boolean existeEmail(String email) {
        return usuariosLista.getUsuarios().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Busca un usuario por su email.
     * @param email El email del usuario a buscar.
     * @return Un Optional con el usuario si existe, Optional.empty() en caso contrario.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuariosLista.getUsuarios().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return La lista de usuarios.
     */
    public UsuariosLista getUsuariosLista() {
        return usuariosLista;
    }

    /**
     * Recarga la lista de usuarios desde el archivo XML.
     */
    public void recargarUsuarios() {
        cargarUsuarios();
    }
}
