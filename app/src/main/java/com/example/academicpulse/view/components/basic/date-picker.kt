package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.model.Field
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
	 * - See also [valid] and [onChangeValidity] to automatically handle the field clearing.
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
	 * val (date, setDate) = useState("")
	 *
	 * Column {
	 * 	Text("Current value is: $date")
	 * 	DatePicker(value = date, onChange = setDate)
	 * }
	 * ```
	 */
	onChange: ((String) -> Unit),

	/** [onChangeValidity] is the short way to change the field validity depending on the field value if it is an empty date.
	 * - Note: It has no effect if [required] is `false`.
	 * ```
	 * Example usage:
	 * val (date, setDate) = useState("")
	 * val (dateValid, setDateValidity) = useState(true)
	 *
	 * Column {
	 * 	DatePicker(
	 * 		value = date,
	 * 		onChange = setDate,
	 * 		required = true,
	 * 		valid = dateValid,
	 * 		onChangeValidity = setDateValidity
	 * 	)
	 * }
	 * ```
	 */
	onChangeValidity: ((Boolean) -> Unit)? = null,
) {
	val (isTempDisabled, setTempDisabled) = useState { false }
	val focusManager = LocalFocusManager.current
	fun onCloseDialog() {
		setTempDisabled(false)
		focusManager.clearFocus()
	}

	val calendarDialogState = rememberSheetState(
		onCloseRequest = { onCloseDialog() },
		onDismissRequest = { onCloseDialog() },
		onFinishedRequest = { onCloseDialog() },
	)

	CalendarDialog(state = calendarDialogState, selection = CalendarSelection.Date { date ->
		onChange(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
	})

	Input(
		value = value,
		label = label,
		placeholder = placeholder,
		required = required,
		valid = valid,
		readOnly = readOnly || isTempDisabled,
		icon = icon,
		focusRequester = focusRequester,
		onChange = {},
		onChangeValidity = onChangeValidity,
		onFocusChange = { focused ->
			if (focused) {
				setTempDisabled(true)
				calendarDialogState.show()
			} else {
				setTempDisabled(false)
				calendarDialogState.hide()
			}
		}
	)
}

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
		onChangeValidity = { field.valid = it },
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
