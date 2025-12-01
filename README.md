Autor: Vicente Javier Bueno Gret
Evaluación Parcial 3 – DSY1105: Desarrollo de Aplicaciones Móviles

Objetivo General

AnimeForumApp es una aplicación móvil nativa para Android, desarrollada con Kotlin y Jetpack Compose, que permite a los usuarios crear, visualizar, editar y eliminar publicaciones relacionadas con series de anime. La aplicación implementa la arquitectura MVVM, persistencia local con Room y consumo de una API externa para entregar sugerencias de anime, demostrando un dominio completo de desarrollo móvil moderno.

Funcionalidades Principales

La aplicación ofrece un sistema completo de publicaciones que incluye creación, lectura, edición y eliminación de posts. El formulario cuenta con validaciones automáticas para asegurar la calidad de los datos ingresados. Además, se integra la API Jikan.moe para obtener sugerencias de anime en tiempo real. La aplicación permite adjuntar imágenes mediante cámara o galería y utilizar entrada por voz a través del micrófono. Los datos se guardan de forma persistente gracias a la base de datos Room. Los posts pueden ser filtrados por categoría y cada publicación incluye un sistema de calificación mediante estrellas. La interfaz está construida completamente en Jetpack Compose, siguiendo principios modernos de diseño con Material 3.

Stack Tecnológico y Arquitectura

El proyecto está desarrollado en Kotlin utilizando Jetpack Compose como tecnología principal para la construcción de interfaces. Se implementa la arquitectura MVVM con ViewModel y StateFlow para el manejo de estado. La persistencia de datos se realiza con Room, mientras que las operaciones asíncronas se gestionan mediante Kotlin Coroutines. Para el consumo de la API se utiliza Retrofit o HttpURLConnection, dependiendo de la versión implementada. Opcionalmente, se puede integrar un sistema de inyección de dependencias como Hilt o Koin.

Estructura del Proyecto

El proyecto está organizado en módulos siguiendo el patrón MVVM:

El módulo data contiene los modelos, el acceso a la base de datos mediante DAOs, el repositorio y los servicios de red.

El módulo ui contiene las pantallas desarrolladas en Jetpack Compose, junto con componentes reutilizables y el sistema de temas.

El módulo viewmodel contiene la lógica de negocio y el manejo de estado que la interfaz consume.

Pasos para Ejecutar el Proyecto

Requisitos previos:

Android Studio versión Koala 2024.1.2 o superior.

Kotlin versión 2.0.0 o superior.

Dispositivo físico o emulador con Android 8.0 (API 26) o superior.

Conexión a internet para la sincronización de dependencias y para el funcionamiento de la API externa.
