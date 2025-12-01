package com.animeforum.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.animeforum.app.ui.components.CategoryChip
import com.animeforum.app.ui.components.PostCard
import com.animeforum.app.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: PostViewModel,
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val posts by viewModel.filteredPosts.collectAsState()
    val category by viewModel.selectedCategory.collectAsState()
    val categories = listOf("Todos", "Shonen", "Seinen", "Shoujo", "Isekai", "Mecha", "Slice of Life")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Anime Forum") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToCreate) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Crear nueva publicación"
                )
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            // Filtros de categorías
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { c ->
                    CategoryChip(
                        category = c,
                        isSelected = c == category,
                        onClick = { viewModel.setSelectedCategory(c) }
                    )
                }
            }

            // Lista de publicaciones
            if (posts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay publicaciones aún.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(posts) { post ->
                        PostCard(
                            post = post,
                            onClick = { onNavigateToDetail(post.id) },
                            onEdit = { onNavigateToEdit(post.id) },
                            onDelete = { viewModel.deletePost(post.id) }
                        )
                    }
                }
            }
        }
    }
}
