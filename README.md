AnimeForumApp

Evaluación Parcial 3 – DSY1105: Desarrollo de Aplicaciones Móviles
Autor:Vicente Javier Bueno Gret  
Proyecto: Aplicación móvil tipo foro de anime  
Framework: Android Studio + Kotlin + Jetpack Compose  
Arquitectura: MVVM + Room + ViewModel + StateFlow  + api 

Objetivo General
Desarrollar una aplicación móvil nativa que permita a los usuarios crear, editar, visualizar y eliminar publicaciones relacionadas con series de anime.  
La app cumple con las buenas prácticas de desarrollo móvil, incorporando validaciones automáticas, animaciones, persistencia local y diseño responsive adaptado a distintos tamaños de pantalla.

Funcionalidades Principales
|Funcionalidad|Descripción|

Crear publicación | Formulario con validaciones automáticas para usuario, anime y mensaje.
Listar publicaciones | Visualización de posts almacenados con diseño Material 3.
Calificación (Rating) | Selección de estrellas con componente personalizado.
Filtrado por categoría | Uso de chips animados (`FilterChip`) para filtrar publicaciones.
Eliminar publicaciones | Menú expandible con animación para eliminar un post.
Persistencia local (Room) | Base de datos local con DAO, Entities y ViewModel usando `StateFlow`.
Modo oscuro / Material 3 | Tema basado en `Theme.Material3.Dark.NoActionBar`. |
Animaciones Compose| Transiciones visuales suaves (`AnimatedVisibility`, `spring`, `tween`)
Ae utilizo API| la cual sirve para realizar recomendacion de anime 
Se integro los sensores nativos| camara, galeria para poder subir imagenes al post y tambien se integro microphone ppara que asi cuente con mas ascesibilidad al realizar el post 


El proyecto sigue el patrón MVVM (Model-View-ViewModel)

Pasos para Ejecutar el Proyecto
Requisitos Previos
Android Studio Koala 🐨 (2024.1.2 o superior)
Kotlin 2.0.20
Compose Compiler 1.6.8
Emulador o dispositivo físico con Android 8.0 (API 26) o superior
Conexión a Internet para sincronizar dependencias de Gradle.
1. Clonar o descargar el proyecto
Puedes descargar el repositorio desde GitHub o importarlo directamente desde Android Studio:
clonar desde repositorio para ejecutar la app de manera local:
git clone https://github.com/Vicentejavier23/AnimeForumApp.git

2. Abrir el proyecto en Android Studio
Abrir Android Studio → seleccionar File → Open
Buscar y abrir la carpeta raíz del proyecto (AnimeForumApp).
Esperar a que se sincronice automáticamente Gradle.
Si aparece un mensaje de sincronización pendiente, presiona Sync Project with Gradle Files.

3. Configurar el emulador
Ir a Tools → Device Manager.
Crear un nuevo dispositivo virtual (ejemplo: Pixel 6).
Elegir una imagen del sistema con Android 14 o 15 (API 34 o 35).
Iniciar el emulador presionando el ícono ▶️ Run.
Si el mensaje “Medium Phone API 36.0 is already running” aparece, significa que el emulador ya está en ejecución correctamente.

4. Ejecutar la aplicación
En la parte superior de Android Studio, asegúrate de que esté seleccionada la configuración “app”.
Pulsa el botón Run ▶️ o usa el atajo Shift + F10.
Android Studio compilará el código, construirá los artefactos Gradle y desplegará la app en el emulador.
Una vez instalada, la aplicación se abrirá automáticamente mostrando la pantalla principal del foro.

5. Probar la funcionalidad
Crear una publicación nueva con todos los campos.
Filtrar por categorías (Shonen, Seinen, etc.).
Eliminar una publicación.
Cerrar y volver a abrir la app para comprobar que los datos se mantienen (persistencia local).
Revisar la animación en los campos con error o al expandir los menús.

6. Ejecución en un dispositivo físico (opcional)
Activar las Opciones de desarrollador en el celular.
Habilitar la Depuración USB.
Conectar el teléfono al computador mediante cable USB.
Android Studio detectará automáticamente el dispositivo.
Seleccionarlo como destino de ejecución y presionar Run.
