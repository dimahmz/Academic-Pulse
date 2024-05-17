package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.academicpulse.utils.useState
import com.example.academicpulse.model.Field
import com.example.academicpulse.theme.gap

@Composable
fun <Item> Select(
	field: Field,

	/** [items] is the list of options rendered withing the dropdown menu.
	 * - See also: [getValue] and [getLabel] if the items are not shown or stored correctly. */
	items: ArrayList<Item>,

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
	 * Select(icon = R.drawable.avatar)
	 * ```
	 */
	icon: Int? = null,

	/** When you have multiple fields in a page that you expect from the user to fill the sequence.
	 * Give them the option of focusing on the next field from the keyboard for better UX.
	 * ```
	 * Example usage:
	 *
	 * Column {
	 * 	val (secondFieldFocus) = useState { FocusRequester() }
	 *
	 * 	Select(focusNext = secondFieldFocus)
	 * 	Input(focusRequester = secondFieldFocus)
	 * }
	 * ```
	 */
	focusNext: FocusRequester? = null,

	// Events

	/** [getValue] is used to store and track the selected item by a unique value like ID. */
	getValue: (it: Item) -> String = { it.toString() },

	/** [getValue] is used to display an item by a meaningful label text for that option. */
	getLabel: (it: Item) -> String = { it.toString() },
) {
	Select(
		value = field.value,
		label = label,
		placeholder = placeholder,
		required = field.required,
		valid = field.valid,
		readOnly = readOnly,
		icon = icon,
		items = items,
		focusRequester = field.focusRequester,
		focusNext = focusNext,
		onChange = { field.value = it },
		getValue = getValue,
		getLabel = getLabel,
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <Item> Select(
	value: String,

	/** [items] is the list of options rendered withing the dropdown menu.
	 * - See also: [getValue] and [getLabel] if the items are not shown or stored correctly. */
	items: ArrayList<Item>,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's `null`, it takes the label value if present. */
	@StringRes placeholder: Int? = null,

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
	 * Select(icon = R.drawable.avatar)
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
	 * 	Select(focusRequester = focusRequester)
	 * }
	 * ```
	 */
	focusRequester: FocusRequester? = null,

	/** When you have multiple fields in a page that you expect from the user to fill the sequence.
	 * Give them the option of focusing on the next field from the keyboard for better UX.
	 * ```
	 * Example usage:
	 *
	 * Column {
	 * 	val (secondFieldFocus) = useState { FocusRequester() }
	 *
	 * 	Select(focusNext = secondFieldFocus)
	 * 	Input(focusRequester = secondFieldFocus)
	 * }
	 * ```
	 */
	focusNext: FocusRequester? = null,

	// Events

	/** [onChange] is invoked when the user change the field value. (Used to update the passed value)
	 * ```
	 * Example usage:
	 * val (type, setType) = useState { "" }
	 *
	 * Column {
	 * 	Text("Current value is: $type")
	 * 	Select(value = type, onChange = setType)
	 * }
	 * ```
	 */
	onChange: ((String) -> Unit),

	/** [getValue] is used to store and track the selected item by a unique value like ID. */
	getValue: (it: Item) -> String = { it.toString() },

	/** [getValue] is used to display an item by a meaningful label text for that option. */
	getLabel: (it: Item) -> String = { it.toString() },
) {
	val (isOpen, setIsOpen) = useState { false }
	val focusManager = LocalFocusManager.current
	fun onCloseDialog() {
		setIsOpen(false)
		focusManager.clearFocus()
		focusNext?.requestFocus()
	}

	val (renderedValue, setRenderedValue) = useState { "" }
	LaunchedEffect(value) {
		if (value == "") return@LaunchedEffect setRenderedValue(value)
		val item = items.find { getValue(it) == value }
		setRenderedValue(if (item == null) value else getLabel(item))
	}

	Box {
		Input(
			value = renderedValue,
			label = label,
			placeholder = placeholder,
			required = required,
			valid = valid,
			readOnly = readOnly || isOpen,
			icon = icon,
			focusRequester = focusRequester,
			onChange = { /* DO NOTHING */ },
			onFocusChange = setIsOpen,
			trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isOpen) },
		)

		DropdownMenu(
			expanded = isOpen,
			onDismissRequest = { onCloseDialog() }
		) {
			items.forEach {
				DropdownMenuItem(
					contentPadding = PaddingValues(horizontal = gap, vertical = 0.dp),
					text = { Text(text = getLabel(it)) },
					onClick = {
						onChange(getValue(it))
						onCloseDialog()
					}
				)
			}
		}
	}
}
