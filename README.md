# 💬 Chat Offline con Gestión de Conversaciones en XML

## 🧠 Descripción general

**Chat Offline** es una aplicación de escritorio desarrollada en **Java 23** utilizando **JavaFX** para la interfaz gráfica, **JAXB** para la persistencia de datos en archivos XML y **Streams** para el análisis y generación de informes.

El sistema permite a los usuarios comunicarse entre sí sin conexión a Internet, almacenando sus mensajes, conversaciones y adjuntos de forma local. Además, ofrece herramientas para exportar y analizar las conversaciones, generando informes de uso, palabras más frecuentes y estadísticas básicas de actividad.

Este proyecto forma parte del **Proyecto Final de la Unidad 1 (XML y Streams)**, dentro del módulo de **Programación de Servicios y Procesos** del ciclo superior en **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

---

## ⚙️ Tecnologías empleadas

- **Java 23**
- **JavaFX** (Interfaz gráfica)
- **JAXB** (Lectura y escritura de XML)
- **Java Streams API** (Análisis y generación de estadísticas)
- **InputStream / OutputStream** (Gestión de archivos adjuntos)
- **ZipOutputStream** (Compresión de conversaciones y adjuntos)
- **Maven** (Gestión de dependencias)
- **Arquitectura MVC**

---

## 🧱 Arquitectura del proyecto

El proyecto sigue una estructura modular basada en el patrón **MVC** (Modelo–Vista–Controlador):

src/
└── main/
├── java/
│ └── com.chatoffline/
│ ├── controller/ # Controladores JavaFX (lógica de interacción)
│ ├── model/ # Clases del modelo de datos (Usuario, Mensaje, etc.)
│ ├── repository/ # Gestión de persistencia con JAXB
│ ├── utils/ # Clases de utilidad (validaciones, streams, etc.)
│ └── Main.java # Punto de entrada
└── resources/
├── fxml/ # Vistas JavaFX
├── data/ # Archivos XML (usuarios, conversaciones)
└── media/ # Carpeta para guardar adjuntos

---

## 💡 Funcionalidades principales

### 👤 1. Registro e inicio de sesión
- Registro de nuevos usuarios con validación de duplicados.
- Inicio de sesión mediante verificación en `usuarios.xml`.
- Control de sesión con un patrón Singleton (un usuario activo a la vez).

### 💬 2. Envío y recepción de mensajes
- Envío de mensajes entre usuarios registrados.
- Persistencia de mensajes en `conversaciones.xml` usando JAXB.
- No se permite eliminar mensajes.
- Los mensajes incluyen fecha, hora y remitente.

### 🗂️ 3. Visualización de conversaciones
- Carga automática de conversaciones previas al iniciar sesión.
- Posibilidad de iniciar nuevos chats o continuar existentes.
- Estructura XML jerárquica por conversación (usuario1 ↔ usuario2).

### 📊 4. Análisis de conversaciones (Streams)
- Cálculo del número total de mensajes por usuario.
- Palabras más utilizadas en la conversación.
- Estadísticas de frecuencia y participación.
- Visualización en gráficos o tablas dentro de la interfaz JavaFX.
- Exportación del informe a texto o CSV.

### 📎 5. Gestión de archivos adjuntos
- Posibilidad de adjuntar imágenes, documentos PDF u otros archivos.
- Guardado local en la carpeta `media/` usando Streams binarios (`InputStream` / `OutputStream`).
- Registro de metadatos del adjunto en el XML (nombre, ruta, tamaño, tipo).
- Validación del tamaño y extensión antes de guardar.
- Si un adjunto falta, el mensaje se conserva mostrando un aviso.

### 🗜️ 6. Exportación de conversaciones y adjuntos
- Exportación de una conversación completa a texto plano (`.txt`).
- Compresión de conversación + adjuntos en un archivo `.zip` usando `ZipOutputStream`.
- Permite archivar y compartir conversaciones fuera del sistema.

---

## 🧭 Flujo de pantallas (JavaFX)

1. **Pantalla de inicio (Login / Registro)**
    - Permite iniciar sesión o crear un nuevo usuario.

2. **Pantalla principal (Gestión de chats)**
    - Muestra la lista de contactos y conversaciones disponibles.
    - Permite iniciar un nuevo chat o continuar uno existente.

3. **Pantalla de chat activo**
    - Área de mensajes y campo de texto.
    - Botón para enviar mensajes y adjuntar archivos.
    - Posibilidad de exportar o ver estadísticas del chat.

4. **Pantalla de estadísticas y exportación**
    - Gráficos, resúmenes y estadísticas generadas con Streams.
    - Opciones de exportación (TXT, ZIP).

---

## 🧩 Persistencia XML

### `usuarios.xml`
Ejemplo de estructura:
```xml
<usuarios>
    <usuario>
        <nombre>María</nombre>
        <apellido>Lopez</apellido>
        <email>maria@mail.com</email>
        <password>1234</password>
    </usuario>
</usuarios>
conversaciones.xml
Ejemplo de estructura:
<conversaciones>
    <conversacion>
        <usuario1>María</usuario1>
        <usuario2>Carlos</usuario2>
        <mensajes>
            <mensaje>
                <remitente>María</remitente>
                <destinatario>Carlos</destinatario>
                <contenido>¡Hola!</contenido>
                <fechaHora>2025-10-09T14:32:00</fechaHora>
                <adjuntoRuta>media/foto1.png</adjuntoRuta>
            </mensaje>
        </mensajes>
    </conversacion>
</conversaciones>
```
## 🧮 Streams aplicados
- Filtrado de mensajes por usuario.
- Contadores de mensajes enviados y recibidos.
- Mapeos para obtener palabras y frecuencias.
- Colecciones para generar informes agrupados.
- Reducciones para sumar estadísticas.
## 📦 Exportaciones disponibles
- TXT	Exportación básica de conversación	Streams
- CSV	Exportación de estadísticas de conversación	Streams
- ZIP	Compresión de conversación + adjuntos	ZipOutputStream