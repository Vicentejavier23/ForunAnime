package com.animeforum.app.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Animación de escala al seleccionar el chip
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "chipScale"
    )

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(category) },
        leadingIcon = if (isSelected) {
            { Icon(Icons.Filled.Check, contentDescription = null) }
        } else null,
        modifier = Modifier.graphicsLayer(
            scaleX = scale,
            scaleY = scale
        )
    )
}
