package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.model.Field
import com.example.academicpulse.theme.*
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
	DatePicker(
		value = field.value,
		label = label,
		placeholder = placeholder,
		required = field.required,
		valid = field.valid,
		readOnly = readOnly,
		icon = icon,
		focusRequester = field.focusRequester,
		onChange = { field.value = it },
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
	value: String,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's `null`, it takes the label value if present. */
	@StringRes placeholder: Int? = R.string.date_placeholder,

	/** [required] indicates if the field should not be empty. If `true`, a red symbol (`*`) added next to the [label].
	 * - Note: It has no effect if [label] is `null`.
	 * - See also [valid] to apply error style on the field borders.
	 */
	required: Boolean = true,

	/** Control the component theme to switch between the main them and error them. */
	valid: Boolean = true,

	/** [readOnly] indicates if the field should accept any changes by the user interaction. */
	readOnly: Boolean = false,

	/** [icon] is the resource ID of the prefix icon.
	 * ```
	 * Example usage:
	 * DatePicker(icon = R.drawable.calendar)
	 * ```
	 */
	icon: Int? = null,

	/** [focusRequester] should be used if there is any external Element or script that can trigger focus on the field.
	 * ```
	 * Example usage:
	 * val (focusRequester) = useState { FocusRequester() }
	 *
	 * Column {
	 * 	Button("Click") { focusRequester.requestFocus() }
	 * 	DatePicker(focusRequester = focusRequester)
	 * }
	 * ```
	 */
	focusRequester: FocusRequester? = null,

	// Events

	/** [onChange] is invoked when the user change the field value. (Used to update the passed value)
	 * ```
	 * Example usage:
	 * val (date, setDate) = useState { "" }
	 *
	 * Column {
	 * 	Text("Current value is: $date")
	 * 	DatePicker(value = date, onChange = setDate)
	 * }
	 * ```
	 */
	onChange: ((String) -> Unit),
) {
	val (isOpen, setIsOpen) = useState { false }
	val focusManager = LocalFocusManager.current
	fun onCloseDialog() {
		setIsOpen(false)
		focusManager.clearFocus()
	}

	val calendarDialogState = rememberSheetState(
		onCloseRequest = { onCloseDialog() },
		onDismissRequest = { onCloseDialog() },
		onFinishedRequest = { onCloseDialog() },
	)

	fun showDialog(state: Boolean) {
		setIsOpen(state)
		if (state) calendarDialogState.show()
		else calendarDialogState.hide()
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
			onChange(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
		})
	}

	Input(
		value = value,
		label = label,
		placeholder = placeholder,
		required = required,
		valid = valid,
		readOnly = readOnly || isOpen,
		icon = icon,
		focusRequester = focusRequester,
		onChange = { /* DO NOTHING */ },
		onFocusChange = ::showDialog,
	)
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDatePicker() {
	val form = useForm()

	Column {
		// Date with today as the initial value.
		val initialDate = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
		val date = useField(form = form, value = initialDate)
		DatePicker(
			field = date,
			label = R.string.date,
		)

		// Date with no initial value
		val date2 = useField(form = form)
		DatePicker(
			field = date2,
			label = R.string.date,
		)
	}
}
