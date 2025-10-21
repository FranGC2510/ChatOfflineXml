package org.dam.fcojavier.chatofflinexml.utils;

/**
 * Clase de utilidad que proporciona métodos estáticos para validar diferentes tipos
 * de datos de usuario, como correos electrónicos y contraseñas.
 * También proporciona mensajes de error predefinidos para cada tipo de validación.
 *
 */
public class Validacion {
    // Constantes para los mensajes de error
    public static final String ERROR_EMAIL = "El formato del email no es válido";
    public static final String ERROR_PASSWORD = "La contraseña debe tener al menos 8 caracteres e incluir mayúsculas, minúsculas y números";

    /**
     * Valida que un email tenga un formato correcto.
     * El formato debe ser: texto@texto.texto
     *
     * @param email El email a validar
     * @return true si el email es válido, false en caso contrario
     */
    public static boolean isValidoEmail(String email) {
        boolean valido = false;
        if (email != null && !email.isEmpty()) {
            valido = email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
        }

        return valido;
    }

    /**
     * Valida si una contraseña cumple con los requisitos de seguridad:
     * - Al menos 8 caracteres.
     * - Al menos una letra minúscula, una letra mayúscula y un número.
     *
     * @param password La contraseña a validar.
     * @return true si la contraseña es válida según los requisitos, false en caso contrario.
     */
    public static boolean isValidaPassword(String password){
        boolean valida = false;
        if (password != null && !password.isEmpty()) {
            valida = password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        }
        return valida;
    }

    /**
     * Valida un email y devuelve un mensaje de error si no es válido.
     *
     * @param email El email a validar
     * @return null si el email es válido, un mensaje de error en caso contrario
     */
    public static String validateEmail(String email) {
        return isValidoEmail(email) ? null : ERROR_EMAIL;
    }

    /**
     * Valida una contraseña y devuelve un mensaje de error si no es válida.
     *
     * @param password La contraseña a validar
     * @return null si la contraseña es válida, un mensaje de error en caso contrario
     */
    public static String validaPassword(String password) {
        return isValidaPassword(password) ? null : ERROR_PASSWORD;
    }

}
