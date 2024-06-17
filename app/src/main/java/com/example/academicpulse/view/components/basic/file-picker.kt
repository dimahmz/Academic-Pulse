package com.example.academicpulse.view.components.basic

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.example.academicpulse.R
import com.example.academicpulse.theme.gap
import com.example.academicpulse.utils.context
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.view_model.Store

val activityLaunchRequest = MutableLiveData(false)

@Composable
fun FilePicker(
	field: Field,

	defaultFileName: String? = null,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's `null`, it takes the label value if present. */
	@StringRes placeholder: Int? = R.string.select_file,

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
) {
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument(),
		onResult = {
			activityLaunchRequest.value = false
			field.focus = false
			field.uri.value = it

			var name = ""
			if (it != null) {
				val cursor = context.contentResolver.query(it, null, null, null, null)
				try {
					if (cursor != null && cursor.moveToFirst()) {
						val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
						name = cursor.getString(index)
					}
				} catch (_: Exception) {
				}
				cursor?.close()
				field.value = name.ifBlank {
					defaultFileName ?: it.path?.substring(it.path!!.lastIndexOf("/") + 1) ?: "File"
				}
			} else field.value = ""
		}
	)

	val focusManager = LocalFocusManager.current
	field.onFocusChange { state ->
		if (state) {
			activityLaunchRequest.value = true
			launcher.launch(mimeTypes)
		} else focusManager.clearFocus()
	}

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
	val file = Field.use(form = null)

	Column(verticalArrangement = Arrangement.spacedBy(gap)) {
		FilePicker(
			field = file,
			label = R.string.date,
			mimeTypes = arrayOf("application/pdf"),
			defaultFileName = "Article",
		)
		Button(text = "Upload file") {
			val uri = file.uri.value
			if (uri != null) Store.files.uploadFile(uri, "file1", {}, {})
		}
	}
}
