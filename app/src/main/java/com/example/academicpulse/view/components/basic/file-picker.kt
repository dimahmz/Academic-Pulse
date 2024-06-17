package com.example.academicpulse.view.components.basic

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.theme.gap
import com.example.academicpulse.utils.context
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view_model.Store

@Composable
fun FilePicker(
	field: Field,

	defaultFileName: String? = null,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's `null`, it takes the label value if present. */
	@StringRes placeholder: Int? = null,

	/** [readOnly] indicates if the field should accept any changes by the user interaction. */
	readOnly: Boolean = false,

	/** [icon] is the resource ID of the prefix icon.
	 * ```
	 * Example usage:
	 * FilePicker(icon = R.drawable.icon_upload_file)
	 * ```
	 */
	icon: Int? = R.drawable.icon_upload_file,

	/** [mimeTypes] is a list of full names of the mime types accepted. */
	mimeTypes: Array<String>,

	uri: Uri?,

	onChangeURI: (Uri?) -> Unit,
) {
	val (firstRender, setFirstRender) = useState { true }
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
			field.value = name.ifBlank {
				defaultFileName ?: uri.path?.substring(uri.path!!.lastIndexOf("/") + 1) ?: "File"
			}
		} else if (!firstRender) field.value = ""
		setFirstRender(false)
	}

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument(),
		onResult = {
			field.focus = false
			onChangeURI(it)
		}
	)

	field.onFocusChange { state -> if (state) launcher.launch(mimeTypes) }

	Input(
		field = field,
		label = label,
		placeholder = placeholder,
		readOnly = readOnly,
		icon = icon,
		centered = true,
	)
}

@Preview
@Composable
fun PreviewFilePicker() {
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
