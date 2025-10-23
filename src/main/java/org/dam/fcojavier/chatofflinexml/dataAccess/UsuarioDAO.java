package org.dam.fcojavier.chatofflinexml.dataAccess;

import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.model.UsuariosLista;
import org.dam.fcojavier.chatofflinexml.utils.XmlManager;

import java.util.Optional;

/**
 * Clase DAO (Data Access Object) para gestionar las operaciones de persistencia de usuarios.
 * Se encarga de leer y escribir la lista de usuarios desde y hacia un archivo XML.
 */
public class UsuarioDAO {
    /**
     * Ruta del archivo XML donde se almacenan los usuarios.
     */
    private static final String USUARIOS_XML = "src/main/resources/data/usuarios.xml";

    private UsuariosLista usuariosLista;

    /**
     * Constructor de la clase UsuarioDAO.
     * Al instanciar un objeto UsuarioDAO, se intenta cargar la lista de usuarios desde el archivo XML.
     * Si el archivo no existe o está vacío, se inicializa una nueva lista de usuarios.
     */
    public UsuarioDAO() {
        cargarUsuarios();
    }

    /**
     * Carga la lista de usuarios desde el archivo XML especificado por {@code USUARIOS_XML}.
     * Si ocurre un error durante la lectura o el archivo está vacío, se inicializa una nueva {@link UsuariosLista}.
     */
    private void cargarUsuarios() {
        usuariosLista = XmlManager.readXML(new UsuariosLista(), USUARIOS_XML);
        if (usuariosLista == null) {
            usuariosLista = new UsuariosLista();
        }
    }

    /**
     * Guarda la lista actual de usuarios en el archivo XML especificado por {@code USUARIOS_XML}.
     * @return {@code true} si la operación de guardado fue exitosa, {@code false} en caso contrario.
     */
    private boolean guardarUsuarios() {
        return XmlManager.writeXML(usuariosLista, USUARIOS_XML);
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Antes de agregar el usuario, verifica si ya existe un usuario con el mismo correo electrónico.
     * Si el usuario se agrega exitosamente, la lista se guarda en el archivo XML.
     * @param usuario El objeto {@link Usuario} a registrar.
     * @return {@code true} si el usuario fue registrado exitosamente, {@code false} si ya existe un usuario con el mismo correo electrónico.
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
     * Verifica si existe un usuario registrado con el correo electrónico proporcionado.
     * La comparación de correos electrónicos no distingue entre mayúsculas y minúsculas.
     * @param email El correo electrónico a buscar.
     * @return {@code true} si existe un usuario con el correo electrónico dado, {@code false} en caso contrario.
     */
    public boolean existeEmail(String email) {
        return usuariosLista.getUsuarios().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Busca un usuario por su correo electrónico.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     * @param email El correo electrónico del usuario a buscar.
     * @return Un {@link Optional} que contiene el objeto {@link Usuario} si se encuentra, o un {@link Optional#empty()} si no existe ningún usuario con ese correo electrónico.
     */
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuariosLista.getUsuarios().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Obtiene la lista completa de usuarios actualmente cargada en el DAO.
     * @return El objeto {@link UsuariosLista} que contiene todos los usuarios registrados.
     */
    public UsuariosLista getUsuariosLista() {
        return usuariosLista;
    }

    /**
     * Recarga la lista de usuarios desde el archivo XML.
     * Esto es útil para actualizar la lista si el archivo XML ha sido modificado externamente.
     */
    public void recargarUsuarios() {
        cargarUsuarios();
    }
}
