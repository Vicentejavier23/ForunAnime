package com.animeforum.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay // ✅ Necesario para usar delay()

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: @Composable () -> Unit,
    error: String?,
    trailingContent: (@Composable () -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    val isError = error != null
    var shake by remember { mutableStateOf(0f) }

    // Animación de "shake" cuando hay error
    val offset = androidx.compose.animation.core.animateFloatAsState(
        targetValue = shake,
        label = "shakeOffset"
    ).value

    LaunchedEffect(isError) {
        if (isError) {
            repeat(3) {
                shake = 10f
                delay(40)
                shake = -10f
                delay(40)
            }
            shake = 0f
        }
    }

    Column(Modifier.offset(x = offset.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = icon,
            trailingIcon = {
                Row {
                    trailingContent?.invoke()

                    AnimatedVisibility(
                        visible = isError,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(
                            Icons.Filled.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            isError = isError,
            singleLine = singleLine,
            maxLines = maxLines,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )

        AnimatedVisibility(
            visible = isError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
