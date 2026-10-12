package com.cse310.studentgradetracker.ui


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * ConfirmDeleteDialog
 *
 * Reusable confirmation dialog for deleting:
 *
 * - Students
 * - Courses
 * - Grades
 *
 * The dialog does not perform SQL operations.
 *
 * It only asks the user to confirm or cancel.
 */
@Composable
fun ConfirmDeleteDialog(
    itemName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(

        // Clicking outside or pressing Escape cancels.
        onDismissRequest = onDismiss,

        title = {
            Text("Confirm Delete")
        },

        text = {
            Text(
                "Are you sure you want to delete $itemName? " +
                        "This action cannot be undone."
            )
        },

        confirmButton = {

            TextButton(
                onClick = onConfirm
            ) {
                Text("Delete")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
