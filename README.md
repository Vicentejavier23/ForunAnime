# 📱 AnimeForumApp
**Evaluación Parcial 3 – DSY1105: Desarrollo de Aplicaciones Móviles**  
**Autor:** Vicente Javier Bueno Gret

---

## 1. 🏷 Nombre del Proyecto
**AnimeForumApp – Aplicación móvil para gestionar publicaciones relacionadas con anime**

---

## 2. 👤 Integrantes
- **Vicente Javier Bueno Gret**

---

## 3. 🚀 Funcionalidades del Proyecto

### 📝 Gestión de Publicaciones (CRUD)
- Crear publicaciones
- Editar publicaciones
- Eliminar publicaciones
- Visualizar detalle de cada publicación

### 🗃 Persistencia Local con Room
- Base de datos interna
- DAOs, Entities y Repositorio implementado
- Persistencia garantizada incluso al cerrar la app

### 🌐 Integración con API Externa (Jikan.moe)
- Obtención de sugerencias de anime en tiempo real
- Búsqueda automatizada por texto

### 🌍 API de Traducción (Google Translate API – Uso Académico)
- Traducción automática de textos al español
- Implementada en Kotlin usando HttpURLConnection y corrutinas

### 🖼 Adjuntar imágenes
- Desde la cámara
- Desde la galería

### 🎤 Entrada por voz
- Dictado automático usando el micrófono del dispositivo

### ⭐ Sistema de calificación
- Rating de 1 a 5 estrellas por publicación

### 🎨 Interfaz Moderna con Jetpack Compose
- Material 3
- Navegación por pantallas
- Componentes reutilizables
- Manejo de estado con ViewModel + StateFlow

---

## 4. 🌐 Endpoints Utilizados (API Externa)

### 📌 4.1. API de Anime – Jikan.moe

#### Endpoint principal utilizado:
GET https://api.jikan.moe/v4/anime?q={query}&limit=10
https://api.jikan.moe/v4/anime?q=naruto&limit=10
___

5. ▶️ Pasos para Ejecutar el Proyecto
   📌 5.1. Requisitos Previos

Android Studio Koala 2024.1.2 o superior
Kotlin 2.0 o superior
Emulador o dispositivo físico Android 8.0 (API 26) o superior
Conexión a Internet (dependencias + APIs externas)

5.2. Clonar el Proyecto
git clone https://github.com/Vicentejavier23/ForunAnime.git
cd ForunAnime

5.3. Abrir en Android Studio
Abrir Android Studio
File → Open
Seleccionar carpeta del proyecto
Esperar la sincronización de Gradle
Ejecutar: Run → Run 'app'

5.4. 🔐 Firma del APK (Release)
El proyecto fue firmado de forma profesional para distribución.
📌 Generación del Keystore
keytool -genkey -v -keystore mi-app-release.keystore -alias mi-app-alias -keyalg RSA -keysize 2048 -validity 10000
📌 Archivo key.properties
(guardado en la raíz del proyecto)
storePassword=********
keyPassword=********
keyAlias=mi-app-alias
storeFile=mi-app-release.keystore
📌 Configuración en app/build.gradle.kts
val keystoreProperties = Properties().apply {
load(FileInputStream(rootProject.file("key.properties")))
}
android {
signingConfigs {
create("release") {
storeFile = file(keystoreProperties["storeFile"]!!)
storePassword = keystoreProperties["storePassword"] as String
keyAlias = keystoreProperties["keyAlias"] as String
keyPassword = keystoreProperties["keyPassword"] as String
}
}
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
📌 Generación del APK firmado
.\gradlew assembleRelease
📁 Ubicación del APK final
app/build/outputs/apk/release/app-release.apk

5.5. Evidencias configuración firma. 
se agrego carpeta llamada evidenciafirma con pantallazo sobre las configuraciones y los archivos