package com.animeforum.app.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.animeforum.app.ui.components.RatingBar
import com.animeforum.app.ui.components.ValidatedTextField
import com.animeforum.app.viewmodel.PostViewModel
import java.io.File
import java.io.FileOutputStream
import java.util.Locale
import android.webkit.MimeTypeMap

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    viewModel: PostViewModel,
    postId: Int?,
    onNavigateBack: () -> Unit
) {
    val s by viewModel.uiState.collectAsState()
    val categories = listOf("Shonen", "Seinen", "Shoujo", "Isekai", "Mecha", "Slice of Life")
    val context = LocalContext.current

    LaunchedEffect(postId) {
        viewModel.loadPostForEdit(postId)
    }

    val speechCallback = remember { mutableStateOf<(String) -> Unit>({}) }
    val speechLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                speechCallback.value(spoken)
            }
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) launchSpeechRecognizer(context, speechLauncher, speechCallback.value)
    }

    fun startVoiceInput(onResult: (String) -> Unit) {
        speechCallback.value = onResult
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            launchSpeechRecognizer(context, speechLauncher, onResult)
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    val cameraFile = remember { mutableStateOf<File?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val file = cameraFile.value
        if (success && file != null) {
            viewModel.addImagePath(file.absolutePath)
        } else {
            file?.delete()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) launchCamera(context, cameraLauncher, cameraFile)
    }

    fun startCameraCapture() {
        val permissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionStatus == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            launchCamera(context, cameraLauncher, cameraFile)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            copyUriToInternalStorage(context, it)?.let(viewModel::addImagePath)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (s.isEditing) "Editar publicación" else "Crear publicación") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ValidatedTextField(
                value = s.username,
                onValueChange = viewModel::updateUsername,
                label = "Usuario",
                icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                error = s.usernameError,
                trailingContent = {
                    IconButton(onClick = { startVoiceInput(viewModel::appendToUsername) }) {
                        Icon(Icons.Filled.Mic, contentDescription = "Dictar usuario")
                    }
                }
            )

            ValidatedTextField(
                value = s.animeTitle,
                onValueChange = viewModel::updateAnimeTitle,
                label = "Anime",
                icon = { Icon(Icons.Filled.Badge, contentDescription = null) },
                error = s.animeTitleError,
                trailingContent = {
                    IconButton(onClick = { startVoiceInput(viewModel::appendToAnimeTitle) }) {
                        Icon(Icons.Filled.Mic, contentDescription = "Dictar título")
                    }
                }
            )

            OutlinedButton(
                onClick = viewModel::fetchAnimeSuggestion,
                enabled = !s.isLoading && !s.isFetchingSuggestion
            ) {
                if (s.isFetchingSuggestion) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Filled.AddAPhoto, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                }
                Text(if (s.isFetchingSuggestion) "Buscando sugerencia..." else "Sugerir anime popular")
            }

            s.suggestedAnimeTitle?.let { title ->
                Card(shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = "Sugerencia: $title", style = MaterialTheme.typography.titleMedium)
                        s.suggestedAnimeSynopsis?.takeIf { it.isNotBlank() }?.let { synopsis ->
                            Text(text = synopsis, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            s.suggestionError?.let { msg ->
                Text(msg, color = MaterialTheme.colorScheme.error)
            }

            ValidatedTextField(
                value = s.message,
                onValueChange = viewModel::updateMessage,
                label = "Mensaje",
                icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                error = s.messageError,
                singleLine = false,
                maxLines = 5,
                trailingContent = {
                    IconButton(onClick = { startVoiceInput(viewModel::appendToMessage) }) {
                        Icon(Icons.Filled.Mic, contentDescription = "Dictar mensaje")
                    }
                }
            )

            Text("Categoría")
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { c ->
                    FilterChip(
                        selected = s.category == c,
                        onClick = { viewModel.updateCategory(c) },
                        label = { Text(c) }
                    )
                }
            }

            s.categoryError?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Text("Calificación")
            RatingBar(
                rating = s.rating,
                onRatingChange = viewModel::updateRating
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.AddAPhoto, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Galería")
                }
                OutlinedButton(onClick = { startCameraCapture() }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Cámara")
                }
            }

            if (s.imagePaths.isNotEmpty()) {
                Text("Imágenes seleccionadas")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(s.imagePaths) { path ->
                        Card(shape = RoundedCornerShape(12.dp)) {
                            Box(modifier = Modifier.size(140.dp)) {
                                Image(
                                    painter = rememberAsyncImagePainter(File(path)),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                IconButton(
                                    onClick = { viewModel.removeImagePath(path) },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar imagen")
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.validateAndSubmitPost(onNavigateBack) },
                enabled = !s.isLoading
            ) {
                Text(
                    when {
                        s.isLoading -> "Guardando..."
                        s.isEditing -> "Guardar cambios"
                        else -> "Publicar"
                    }
                )
            }
        }
    }
}

private fun launchSpeechRecognizer(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>,
    onResult: (String) -> Unit
) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora")
    }
    launcher.launch(intent)
}

private fun launchCamera(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Uri>,
    cameraFile: MutableState<File?>
) {
    val file = createImageFile(context)
    cameraFile.value = file
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    launcher.launch(uri)
}

private fun createImageFile(context: Context): File {
    val dir = File(context.filesDir, "images").apply { if (!exists()) mkdirs() }
    return File(dir, "photo_${System.currentTimeMillis()}.jpg")
}

private fun copyUriToInternalStorage(context: Context, uri: Uri): String? {
    return runCatching {
        val dir = File(context.filesDir, "images").apply { if (!exists()) mkdirs() }
        val extension = resolveExtension(context, uri) ?: "jpg"
        val destination = File(dir, "gallery_${System.currentTimeMillis()}.$extension")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destination).use { output ->
                input.copyTo(output)
            }
        }
        destination.absolutePath
    }.getOrNull()
}

private fun resolveExtension(context: Context, uri: Uri): String? {
    val mimeType = context.contentResolver.getType(uri)
    val fromMime = mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
    if (!fromMime.isNullOrBlank()) return fromMime

    val rawPath = uri.lastPathSegment ?: return null
    val dotIndex = rawPath.lastIndexOf('.')
    return if (dotIndex != -1 && dotIndex < rawPath.lastIndex) {
        rawPath.substring(dotIndex + 1)
    } else {
        null
    }
}
