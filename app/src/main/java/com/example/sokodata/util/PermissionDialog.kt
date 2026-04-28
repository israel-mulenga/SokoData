package com.example.sokodata.util

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * Dialog pour demander les permissions nécessaires
 */
@Composable
fun PermissionRequestDialog(
    title: String,
    message: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(onClick = onPermissionGranted) {
                Text("Accepter")
            }
        },
        dismissButton = {
            TextButton(onClick = onPermissionDenied) {
                Text("Refuser")
            }
        }
    )
}
