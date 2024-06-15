package com.example.academicpulse.view.pages.home

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.academicpulse.utils.context
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*

@Composable
fun TestPage() {
	val (uri, setURI) = useState<Uri?> { null }

	val (fileName, setFileName) = useState { "" }
	LaunchedEffect(uri) {
		// Get the file name
		var name = ""
		if (uri != null) {
			val cursor = context.contentResolver.query(uri, null, null, null, null)
			try {
				if (cursor != null && cursor.moveToFirst()) {
					val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
					name = cursor.getString(index)
				}
			} catch (_: Exception) {
			}
			cursor?.close()
		}
		setFileName(name.ifBlank { uri?.path?.substring(uri.path!!.lastIndexOf("/") + 1) ?: "" })
	}

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument(),
		onResult = { setURI(it) }
	)

	Column {
		Text(text = "Select file")
		Button(text = "+") {
			launcher.launch(arrayOf("application/pdf"))
		}
		Text(text = "File name: $fileName")
	}
}
