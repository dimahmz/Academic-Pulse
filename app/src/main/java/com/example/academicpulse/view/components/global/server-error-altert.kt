package com.example.academicpulse.view.components.global

import 	androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.academicpulse.R
import com.example.academicpulse.view.components.basic.Text


@Composable
fun ServerErrorAlertDialog(
	onConfirmation: () -> Unit,
) {
	AlertDialog(title = {
		Text(text = R.string.server_error_title)
	}, text = {
		Text(text = R.string.server_error_description)
	}, onDismissRequest = {}, confirmButton = {
		TextButton(onClick = {
			onConfirmation()
		}) {
			Text(text = R.string.close)
		}
	}, dismissButton = {})
}
