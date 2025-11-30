package com.animeforum.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.animeforum.app.data.model.Post
import com.animeforum.app.utils.NativeResourceManager
import com.animeforum.app.utils.VibrationType
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostCard(
    post: Post,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val nativeResources = remember(context) { NativeResourceManager(context) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- Fila Superior: Usuario y Menú ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Usuario",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = post.username,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                // Botón para expandir las opciones (ej. Eliminar, Editar)
                IconButton(onClick = {
                    expanded = !expanded
                    nativeResources.vibrateOnAction(VibrationType.SHORT)
                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Título del Anime ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Movie,
                    contentDescription = "Título del anime",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = post.animeTitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Mensaje del Post ---
            Text(
                text = post.message,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Fila Inferior: Categoría y Rating ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(post.category) }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(post.rating) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Estrella de calificación",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Menú expandible (Eliminar) ---
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(200)),
                exit = shrinkVertically(animationSpec = tween(200))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        nativeResources.vibrateOnAction(VibrationType.MEDIUM)
                        expanded = false
                        onEdit()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar publicación"
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Editar")
                    }
                    TextButton(onClick = {
                        nativeResources.vibrateOnAction(VibrationType.LONG)
                        expanded = false
                        onDelete()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar publicación"
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Eliminar")
                    }
                }
            }

            // --- Fecha alineada a la derecha ---
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(Date(post.timestamp)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}
