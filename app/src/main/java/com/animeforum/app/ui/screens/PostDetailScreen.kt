package com.animeforum.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.animeforum.app.viewmodel.PostViewModel

// Import the OptIn annotation
import androidx.compose.material3.ExperimentalMaterial3Api

// Apply the OptIn annotation here
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    postId: Int,
    viewModel: PostViewModel,
    onNavigateBack: () -> Unit,
    onEditPost: (Int) -> Unit
) {
    val posts by viewModel.allPosts.collectAsState()
    val post = posts.firstOrNull { it.id == postId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(post?.animeTitle ?: "Detalle") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (post == null) {
                Text("Publicación no encontrada")
            } else {
                Text("Autor: " + post.username, style = MaterialTheme.typography.titleSmall)
                Text(post.message)
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    viewModel.deletePost(postId)
                    onNavigateBack()
                }) {
                    Text("Eliminar publicación")
                }

                Button(onClick = { onEditPost(postId) }) {
                    Text("Editar publicación")
                }
            }
        }
    }
}
