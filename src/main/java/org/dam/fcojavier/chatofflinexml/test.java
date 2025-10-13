package org.dam.fcojavier.chatofflinexml;

import org.dam.fcojavier.chatofflinexml.model.Usuario;
import org.dam.fcojavier.chatofflinexml.model.UsuariosLista;
import org.dam.fcojavier.chatofflinexml.utils.XmlManager;

import javax.xml.bind.JAXBException;

public class test {
    public static void main(String[] args) {
        testGuardarDosUsuarios();
    }

    /**
     * Test para guardar dos usuarios en un archivo XML
     */
    public static void testGuardarDosUsuarios() {
        System.out.println("=== Test: Guardar dos usuarios en XML ===\n");

        // Crear el contenedor de usuarios
        UsuariosLista usuarios = new UsuariosLista();

        // Crear el primer usuario
        Usuario usuario1 = new Usuario(
                "María",
                "López",
                "maria@mail.com",
                "1234"
        );

        // Crear el segundo usuario
        Usuario usuario2 = new Usuario(
                "Carlos",
                "García",
                "carlos@mail.com",
                "5678"
        );

        // Añadir usuarios al contenedor
        usuarios.addUsuario(usuario1);
        usuarios.addUsuario(usuario2);

        System.out.println("Usuarios a guardar:");
        System.out.println(usuario1);
        System.out.println(usuario2);
        System.out.println();

        // Definir la ruta del archivo XML
        String rutaXml = "src/main/resources/data/usuarios.xml";

        // Guardar en XML
        boolean guardado = XmlManager.writeXML(usuarios, rutaXml);

        if (guardado) {
            System.out.println("✓ Usuarios guardados exitosamente en: " + rutaXml);
        } else {
            System.out.println("✗ Error al guardar usuarios");
        }

        System.out.println("\n=== Test completado ===");

        // Opcionalmente, leer el archivo para verificar
        testLeerUsuarios(rutaXml);
    }

    /**
     * Test para leer usuarios desde un archivo XML
     */
    public static void testLeerUsuarios(String rutaXml) {
        System.out.println("\n=== Test: Leer usuarios desde XML ===\n");

        // Leer el XML
        UsuariosLista usuariosLeidos = XmlManager.readXML(new UsuariosLista(), rutaXml);

        if (usuariosLeidos != null) {
            System.out.println("✓ Usuarios leídos exitosamente");
            System.out.println("Total de usuarios: " + usuariosLeidos.getUsuarios().size());
            System.out.println();

            // Mostrar cada usuario
            for (int i = 0; i < usuariosLeidos.getUsuarios().size(); i++) {
                Usuario u = usuariosLeidos.getUsuarios().toArray(new Usuario[0])[i];
                System.out.println("Usuario " + (i + 1) + ":");
                System.out.println("  Nombre: " + u.getNombre());
                System.out.println("  Apellido: " + u.getApellido());
                System.out.println("  Email: " + u.getEmail());
                System.out.println("  Password: " + u.getPassword());
                System.out.println();
            }
        } else {
            System.out.println("✗ Error al leer usuarios");
        }

        System.out.println("=== Test completado ===");
    }
}
