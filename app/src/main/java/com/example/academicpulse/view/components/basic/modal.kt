package com.example.academicpulse.view.components.basic

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.academicpulse.R
import com.example.academicpulse.theme.error
import com.example.academicpulse.theme.primary
import com.example.academicpulse.theme.white
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
	@StringRes cancelText: Int = R.string.cancel,
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
