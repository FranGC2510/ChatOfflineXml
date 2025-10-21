package org.dam.fcojavier.chatofflinexml.utils;


import org.dam.fcojavier.chatofflinexml.model.Usuario;

/**
 * Clase que implementa el patrón Singleton para gestionar la sesión del usuario actual en el sistema.
 * Garantiza que solo exista una única instancia de la sesión en toda la aplicación.
 */
public class SesionUsuario {
    private static SesionUsuario instance;
    private Usuario usuarioActual;

    /**
     * Constructor privado para implementar el patrón Singleton.
     * Evita que se puedan crear instancias desde fuera de la clase.
     */
    private SesionUsuario() {}

    /**
     * Obtiene la única instancia de SesionUsuario (patrón Singleton).
     * Si no existe la instancia, la crea.
     * @return La única instancia de SesionUsuario
     */
    public static SesionUsuario getInstance() {
        if(instance == null) {
            instance = new SesionUsuario();
        }
        return instance;
    }

    /**
     * Obtiene el usuario que tiene la sesión iniciada actualmente.
     * @return El usuario con sesión activa, o null si no hay sesión iniciada
     */
    public Usuario getUsuarioActual() {
        return this.usuarioActual;
    }

    /**
     * Establece el usuario actual de la sesión.
     * @param usuarioActual El usuario a establecer como actual, puede ser null
     */
    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    /**
     * Inicia la sesión para un usuario específico.
     * @param usuario El usuario que iniciará sesión
     */
    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    /**
     * Cierra la sesión del usuario actual estableciendo usuarioActual a null.
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    /**
     * Verifica si hay un usuario con sesión activa.
     * @return true si hay un usuario conectado, false en caso contrario.
     */
    public boolean haySesionActiva() {return usuarioActual != null;}
}
