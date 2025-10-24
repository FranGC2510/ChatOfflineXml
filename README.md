# Chat Offline XML 

**Chat Offline XML** es una aplicación de escritorio desarrollada en JavaFX que simula un sistema de chat persistente. A diferencia de un chat en tiempo real, esta aplicación funciona de manera asíncrona ("offline"), almacenando todas las conversaciones y perfiles de usuario localmente en archivos XML.

La aplicación permite a los usuarios registrarse, iniciar sesión de forma segura y mantener conversaciones que se guardan automáticamente. Incluye funcionalidades avanzadas como el envío de archivos adjuntos, un panel de estadísticas de conversación (generado con la API de Streams) y múltiples formatos de exportación.

---

## Características Principales

Este proyecto implementa todas las funcionalidades requeridas:

* **Autenticación de Usuarios:**
    * Sistema completo de **Registro** e **Inicio de Sesión**.
    * **Seguridad:** Las contraseñas se almacenan *hasheadas* utilizando **jBCrypt**, nunca en texto plano.
    * Validación de formato de email y fortaleza de contraseña durante el registro.

* **Sistema de Chat Persistente:**
    * Las conversaciones se guardan automáticamente.
    * Los mensajes antiguos se cargan al seleccionar un chat.
    * Interfaz de usuario con burbujas de chat diferenciadas para emisor y receptor.

* **Gestión de Archivos Adjuntos:**
    * Permite adjuntar archivos a los mensajes (imágenes, PDF, DOC, ZIP, etc.).
    * **Validación:** Comprueba el tamaño (máx 10MB) y la extensión del archivo antes de enviarlo.
    * **Previsualización:** Muestra una vista previa de las imágenes en la burbuja del chat y un icono específico para otros tipos de archivo (PDF, DOC, TXT, ZIP).
    * Los archivos adjuntos son **clickables** y se abren con la aplicación por defecto del sistema.
    * Maneja correctamente los **adjuntos faltantes** (si el archivo se borra de la carpeta `media/`), mostrando un aviso sin romper el chat.

* **Análisis y Estadísticas (con API Streams):**
    * Cada conversación tiene una ventana de **Estadísticas**.
    * La analítica se realiza al 100% con la **API de Streams**.
    * Muestra:
        1.  **Total de Mensajes**.
        2.  **Participación por Usuario** (en un Gráfico de Tarta).
        3.  **Palabras más Usadas** (en una Tabla).

* **Exportación de Conversaciones:**
    * Permite exportar el historial de chat a **TXT** y **CSV**.
    * **¡Función Opcional Implementada!** Permite exportar la conversación a un archivo **ZIP**. Este ZIP incluye el historial en `.txt` y una carpeta `media/` con **todos los archivos adjuntos** de esa conversación.

 ---

## Interfaz de Usuario y Experiencia (UI/UX)

Además de la robusta funcionalidad backend, se ha puesto un especial énfasis en crear una interfaz de usuario moderna, intuitiva y estéticamente agradable.

*   **Diseño Moderno y Coherente:**
    *   Toda la aplicación, desde el inicio de sesión hasta el chat, comparte una paleta de colores y un estilo visual unificado.
    *   Uso de degradados sutiles y sombras para crear una sensación de profundidad y profesionalismo.

*   **Componentes Estilizados:**
    *   **Formularios Flotantes:** Las pantallas de Login y Registro presentan los formularios en "tarjetas" con bordes redondeados y sombras.
    *   **Campos de Texto Mejorados:** Todos los campos de texto tienen un diseño de "píldora" y un efecto visual al ser seleccionados para mejorar la usabilidad.
    *   **Lista de Usuarios Atractiva:** La lista de usuarios muestra un avatar junto al nombre completo y utiliza un efecto "burbuja" para la selección y el hover. Esto se logra mediante una clase `ListCell` personalizada (`UsuarioListCell`) que define la estructura de cada elemento.

*   **Chat Visualmente Atractivo:**
    *   **Burbujas de Chat con Estilo:** Los mensajes enviados y recibidos tienen degradados de color y sombras para una fácil distinción y una apariencia pulida.
    *   **Mensajes de Bienvenida Centrados:** En chats vacíos, se muestra un mensaje de bienvenida amigable y centrado.

---

## Flujo de Pantallas

La navegación de la aplicación sigue la siguiente lógica:

1.  **Inicio:** La aplicación se inicia en la pantalla de **Login** (`login-view.fxml`).
2.  **Desde Login, hay dos caminos:**
    * **A) Iniciar Sesión:**
        * El usuario introduce sus credenciales.
        * `LoginController` valida los datos contra `usuarios.xml` usando `UsuarioDAO` y `PasswordUtilidades`.
        * Si el login es correcto, se guarda el usuario en el Singleton `SesionUsuario`, se cierra la ventana de Login y se abre la pantalla principal de **Chat** (`ChatView.fxml`).
    * **B) Registrarse:**
        * El usuario pulsa "Registrarse".
        * `LoginController` abre la pantalla de **Registro** (`registro-view.fxml`) como una ventana modal.
        * Tras rellenar el formulario, `RegistroController` valida los datos, crea el usuario y lo guarda en `usuarios.xml`.
        * Si el registro es exitoso, **inicia sesión automáticamente** (guardando en `SesionUsuario`), cierra las ventanas de Registro y Login, y abre la pantalla principal de **Chat** (`ChatView.fxml`).
3.  **Pantalla de Chat (`ChatView.fxml`):**
    * Esta es la interfaz principal. El `ChatViewController` carga la lista de usuarios (excluyendo al propio usuario).
    * Al seleccionar un usuario, se carga la conversación (o un mensaje de bienvenida si está vacía) usando `ConversacionDAO`.
    * El usuario puede enviar mensajes, adjuntar archivos y ver el historial.
4.  **Desde el Chat, hay tres caminos:**
    * **A) Ver Estadísticas:**
        * El usuario pulsa "Estadísticas".
        * Se abre la ventana modal de **Estadísticas** (`EstadisticasView.fxml`).
        * `EstadisticasController` recibe la conversación actual y la analiza usando `AnalizadorConversacion` para mostrar los gráficos y tablas.
    * **B) Exportar:**
        * El usuario pulsa "Exportar".
        * Se muestra un diálogo para elegir el formato (TXT, CSV, ZIP).
        * Se procede a exportar la conversación actual.
    * **C) Cerrar Sesión:**
        * El usuario pulsa "Cerrar Sesión".
        * Se limpia el `SesionUsuario`.
        * Se cierra la ventana de Chat y se vuelve a abrir la pantalla de **Login** (`login-view.fxml`), reiniciando el flujo.

---

## Tecnologías Utilizadas

* **Java 23**
* **JavaFX:** Para toda la interfaz gráfica de usuario (GUI).
* **JAXB (Java Architecture for XML Binding):** Utilizado para serializar (escribir) y deserializar (leer) los objetos de Java (`Usuario`, `Conversacion`, etc.) a y desde archivos XML.
* **Java Streams API:** Usada intensivamente para el análisis de conversaciones y la exportación de datos.
* **jBCrypt:** Librería para el hasheo y verificación segura de contraseñas.
* **Maven:** Para la gestión de dependencias y la construcción del proyecto.

---

## Arquitectura y Almacenamiento de Datos

La persistencia de datos se gestiona íntegramente con archivos XML y un directorio para adjuntos.

* **Patrón DAO (Data Access Object):** Se utiliza para separar la lógica de negocio del acceso a datos.
    * `UsuarioDAO`: Se encarga de leer y escribir en `usuarios.xml`.
    * `ConversacionDAO`: Gestiona la lectura y escritura de los archivos de conversación individuales.

* **Modelo de Datos (JAXB):** Clases Puras de Java (POJOs) anotadas para JAXB que modelan la información.
    * `Usuario`, `UsuariosLista`, `Conversacion`, `Mensaje`, `Adjunto`.

* **Estructura de Almacenamiento:**
    * `src/main/resources/data/usuarios.xml`: Archivo XML único que almacena la lista de **todos** los usuarios registrados.
    * `src/main/resources/data/conversaciones/`: Directorio que almacena **un archivo XML por cada conversación**. El nombre del archivo se genera ordenando alfabéticamente los nombres de los dos participantes (ej. `Fran_Maria.xml`) para evitar duplicados.
    * `media/`: Directorio (creado en la raíz del proyecto al ejecutar) donde se copian **todos** los archivos adjuntos enviados.