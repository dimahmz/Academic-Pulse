package com.example.academicpulse.view.pages.publication


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.h1TextSize
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Modal
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.LoaderScreen
import com.example.academicpulse.view_model.Store

@Composable
fun PublicationSettings() {
	val publication = useAtom(Store.publications.publication)
	val (isOpen, setIsOpen) = useState { false }
	val (isLoading, setIsLoading) = useState { false }

	fun onCloseDialog() {
		setIsOpen(false)
	}

	fun onOpenDialog() {
		setIsOpen(true)
	}

	val confirm = Modal(
		R.string.delete_title, R.string.delete_message, R.string.delete_cancel, R.string.delete_confirm
	) {
		setIsLoading(true)
		Store.publications.deleteById(
			onSuccess = {
				Router.navigate("profile", false)
				setIsLoading(false)
			},
			onError = { setIsLoading(false) }
		)
	}
	if (isLoading) LoaderScreen()
	Box {
		Icon(id = R.drawable.icon_settings,
			color = MaterialTheme.colorScheme.primary,
			size = (h1TextSize.value * 20 / 23).dp,
			onClick = ::onOpenDialog
		)
		DropdownMenu(expanded = isOpen, onDismissRequest = { onCloseDialog() }) {
			DropdownMenuItem(contentPadding = PaddingValues(horizontal = gap, vertical = 0.dp),
				text = { Text(text = "Delete") },
				onClick = {
					confirm.show()
					onCloseDialog()
				}
			)
		}
	}
}
