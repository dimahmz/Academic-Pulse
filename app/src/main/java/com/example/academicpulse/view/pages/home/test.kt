package com.example.academicpulse.view.pages.home

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.academicpulse.R
import com.example.academicpulse.theme.gap
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@Composable
fun TestPage() {
	val (uri, setURI) = useState<Uri?> { null }
	val file = Field.use(form = null)

	LaunchedEffect(Unit) {
		Store.files.readFile("file1", {}, {})
	}

	Column(verticalArrangement = Arrangement.spacedBy(gap)) {
		FilePicker(
			field = file,
			label = R.string.date,
			mimeTypes = arrayOf("application/pdf"),
			defaultFileName = "Article",
			uri = uri,
			onChangeURI = setURI
		)
		Button(text = "Upload file") {
			if (uri != null) Store.files.uploadFile(uri, "file1", {}, {})
		}
	}
}
