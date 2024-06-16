package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.theme.*
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.logcat
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
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
	val focusManager = LocalFocusManager.current
	val calendarDialogState = rememberSheetState(onCloseRequest = { field.focus = false })
	field.onFocusChange(fun (state: Boolean) {
		if (state) calendarDialogState.show()
		else {
			focusManager.clearFocus()
			calendarDialogState.hide()
		}
	})

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

	Input(
		field = field,
		label = label,
		placeholder = placeholder,
		readOnly = readOnly,
		icon = icon,
	)
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDatePicker() {
	val form = Form.use()

	Column {
		// Date with today as the initial value.
		val initialDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
		val date = Field.use(form = form, value = initialDate)
		DatePicker(
			field = date,
			label = R.string.date,
		)

		// Date with no initial value
		val date2 = Field.use(form = form)
		DatePicker(
			field = date2,
			label = R.string.date,
		)
	}
}
