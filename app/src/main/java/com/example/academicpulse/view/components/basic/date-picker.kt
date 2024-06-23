package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import com.example.academicpulse.R
import com.example.academicpulse.theme.*
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.useAtom
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
	field: Field,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's `null`, it takes the label value if present. */
	@StringRes placeholder: Int? = R.string.date_placeholder,

	/** [readOnly] indicates if the field should accept any changes by the user interaction. */
	readOnly: Boolean = false,

	/** [icon] is the resource ID of the prefix icon.
	 * ```
	 * Example usage:
	 * DatePicker(icon = R.drawable.calendar)
	 * ```
	 */
	icon: Int? = null,
) {
	val onFocus: ((Boolean) -> Unit)?
	if (useAtom(activityLaunchRequest, false)) {
		onFocus = null
	} else {
		val calendarDialogState = rememberSheetState(onCloseRequest = { field.focus = false })
		val focusManager = LocalFocusManager.current
		onFocus = { state ->
			if (state) calendarDialogState.show()
			else {
				focusManager.clearFocus()
				calendarDialogState.hide()
			}
		}
		field.onFocusChange { state ->
			if (state) calendarDialogState.show()
			else {
				focusManager.clearFocus()
				calendarDialogState.hide()
			}
		}
		MaterialTheme(
			colorScheme = lightColorScheme(
				surface = white, // Background color
				onSurface = textColor, // Color of texts
				primary = primary, // Color of "cancel" and today date texts, Background color of selected date
				onPrimary = white, // Color of arrows, background color of selected date text
				secondaryContainer = primary, // Background color of arrows
			),
		) {
			CalendarDialog(state = calendarDialogState, selection = CalendarSelection.Date { date ->
				field.value = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
			})
		}
	}

	field.onFocusChange { state -> onFocus?.invoke(state) }
	Input(
		field = field,
		label = label,
		placeholder = placeholder,
		readOnly = readOnly,
		icon = icon,
	)
}
