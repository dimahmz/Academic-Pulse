package com.example.academicpulse.view.components.global


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.h1TextSize
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Modal
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view_model.Store

@Composable
fun PublicationSettings(onDelete: () -> Unit) {
	val (isOpen, setIsOpen) = useState { false }

	val confirm = Modal(
		R.string.delete_title, R.string.delete_message, R.string.delete_cancel, R.string.delete_confirm
	) {
		Store.isLoading.value = true
		Store.publications.deleteById(
			onError = { Store.isLoading.value = false },
			onSuccess = onDelete,
		)
	}

	Box {
		Icon(
			id = R.drawable.icon_settings,
			color = MaterialTheme.colorScheme.primary,
			size = (h1TextSize.value * 20 / 23).dp,
			onClick = { setIsOpen(true) }
		)
		DropdownMenu(expanded = isOpen, onDismissRequest = { setIsOpen(false) }) {
			DropdownMenuItem(contentPadding = PaddingValues(horizontal = gap, vertical = 0.dp),
				text = { Text(text = "Delete") },
				onClick = {
					confirm.show()
					setIsOpen(false)
				}
			)
		}
	}
}
