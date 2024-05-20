package com.example.academicpulse.view.components.basic

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.theme.error
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.primary
import com.example.academicpulse.theme.white
import com.example.academicpulse.utils.logcat
import com.maxkeppeker.sheets.core.models.base.ButtonStyle
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.SheetState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.info.InfoDialog
import com.maxkeppeler.sheets.info.models.InfoBody
import com.maxkeppeler.sheets.info.models.InfoSelection

@SuppressLint("ComposableNaming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Modal(
	@StringRes title: Int,
	@StringRes message: Int,
	@StringRes cancelText: Int,
	@StringRes okText: Int,
	danger: Boolean = false,
	onCancel: (() -> Unit)? = null,
	onOk: () -> Unit
): SheetState {
	val dialogState = rememberSheetState(onDismissRequest = { onCancel?.invoke() })
	MaterialTheme(
		colorScheme = lightColorScheme(
			primary = if (danger) error else primary,
			onPrimary = white,
		),
	) {
		InfoDialog(
			state = dialogState,
			body = InfoBody.Custom {
				H2(text = title)
				Text(text = message)
			},
			selection = InfoSelection(
				negativeButton = SelectionButton(
					text = stringResource(cancelText),
					type = ButtonStyle.OUTLINED
				),
				positiveButton = SelectionButton(
					text = stringResource(okText),
					type = ButtonStyle.FILLED
				),
				onNegativeClick = { onCancel?.invoke() },
				onPositiveClick = { onOk() },
			),
		)
	}
	return dialogState
}

@Preview(showSystemUi = true)
@Composable
fun PreviewModal() {
	val confirm = Modal(
		title = R.string.title,
		message = R.string.about_description_1,
		cancelText = R.string.skip,
		okText = R.string.continued,
	) {
		logcat("on confirm")
	}
	val danger = Modal(
		title = R.string.title,
		message = R.string.about_description_1,
		cancelText = R.string.skip,
		okText = R.string.continued,
		danger = true,
		onCancel = { logcat("Optional callback") },
	) {
		logcat("on confirm danger")
	}

	Column(verticalArrangement = Arrangement.spacedBy(gap)) {
		Button(text = "Confirm modal") {
			logcat("before show confirm")
			confirm.show()
		}
		Button(text = "Danger modal", onClick = danger::show)
	}
}
