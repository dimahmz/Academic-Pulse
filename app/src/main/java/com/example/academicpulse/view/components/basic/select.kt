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
import androidx.compose.ui.unit.dp
import com.example.academicpulse.utils.useState
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.theme.gap

@OptIn(ExperimentalMaterial3Api::class)
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

	// Events

	/** [getValue] is used to store and track the selected item by a unique value like ID. */
	getValue: (it: Item) -> String = { it.toString() },

	/** [getValue] is used to display an item by a meaningful label text for that option. */
	getLabel: (it: Item) -> String = { it.toString() },
) {
	val (isOpen, setIsOpen) = useState { false }
	field.onFocusChange(setIsOpen)

	val (renderedValue, setRenderedValue) = useState { "" }
	LaunchedEffect(field.value) {
		if (field.value == "") return@LaunchedEffect setRenderedValue(field.value)
		val item = items.find { getValue(it) == field.value }
		setRenderedValue(if (item == null) field.value else getLabel(item))
	}

	Box {
		Input(
			field = field,
			renderedValue = renderedValue,
			label = label,
			placeholder = placeholder,
			readOnly = readOnly,
			icon = icon,
			trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isOpen) },
		)

		DropdownMenu(
			expanded = isOpen,
			onDismissRequest = { field.focus = false }
		) {
			items.forEach {
				DropdownMenuItem(
					contentPadding = PaddingValues(horizontal = gap, vertical = 0.dp),
					text = { Text(text = getLabel(it)) },
					onClick = {
						field.value = getValue(it)
						field.focus = false
					}
				)
			}
		}
	}
}
