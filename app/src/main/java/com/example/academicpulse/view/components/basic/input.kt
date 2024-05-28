package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.academicpulse.R
import com.example.academicpulse.theme.inputGray
import com.example.academicpulse.theme.inputHeight
import com.example.academicpulse.theme.inputLabelGap
import com.example.academicpulse.theme.radius
import com.example.academicpulse.model.Field
import com.example.academicpulse.theme.textColor
import com.example.academicpulse.utils.useState

@Composable
fun Input(
	value: String,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's null, it takes the label value if present. */
	@StringRes placeholder: Int? = null,

	/** [required] indicates if the field should not be empty. If `true`, a red symbol (`*`) added next to the [label].
	 * - Note: It has no effect if [label] is `null`.
	 * - See also [valid] and [onChangeValidity] to automatically handle the field clearing.
	 */
	required: Boolean = true,

	/** Control the component theme to switch between the main them and error them. */
	valid: Boolean = true,

	/** [readOnly] indicates if the field should accept any changes by the user interaction. */
	readOnly: Boolean = false,

	/** [password] indicates if the field is a password or not. If `true`, a custom hide/show icons with their logic are applied. */
	password: Boolean = false,

	/** [icon] is the resource ID of the prefix icon.
	 * ```
	 * Example usage:
	 * Input(placeholder = "Search", icon = R.drawable.icon_search)
	 * ```
	 */
	icon: Int? = null,

	trailingIcon: @Composable (() -> Unit)? = null,

	/** [okIcon] is the icon type of the keyboard OK button, e.g. `ImeAction.Done`, `ImeAction.Next`, `ImeAction.Send`
	 * - See also [onOk] to set a custom behavior when clicking on this button
	 */
	okIcon: ImeAction = ImeAction.Default,

	/** If the [onOk] callback is present, by default the keyboard will be hidden before executing it.
	 * [hideKeyboardOnOk] is to control the keyboard to stay visible or not.
	 */
	hideKeyboardOnOk: Boolean = true,

	/** [keyboardType] is the keyboard type, e.g. `KeyboardType.Text`, `KeyboardType.Email`, `KeyboardType.Decimal`
	 * - Note: It has no effect if [password] is `true`, as `KeyboardType.Password` is applied automatically.
	 */
	keyboardType: KeyboardType = KeyboardType.Text,

	/** [focusRequester] should be used if there is any external Element or script that can trigger focus on the field.
	 * ```
	 * Example usage:
	 * val (focusRequester) = useState { FocusRequester() }
	 *
	 * Column {
	 * 	Button("Click") { focusRequester.requestFocus() }
	 * 	Input(focusRequester = focusRequester)
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
	 * 	Input(focusNext = secondFieldFocus)
	 * 	Input(focusRequester = secondFieldFocus)
	 * }
	 * ```
	 */
	focusNext: FocusRequester? = null,

	// Events

	/** [onChange] is invoked when the user change the field value. (Used to update the passed value)
	 * ```
	 * Example usage:
	 * val (message, setMessage) = useState { "" }
	 *
	 * Column {
	 * 	Text("Current value is: $message")
	 * 	Input(value = message, onChange = setMessage)
	 * }
	 * ```
	 */
	onChange: (String) -> Unit,

	/** [onChangeValidity] is the short way to change the field validity depending on the field value if it is an empty string.
	 * - Note: It has no effect if [required] is `false`.
	 * ```
	 * Example usage:
	 * val (message, setMessage) = useState { "" }
	 * val (messageValid, setMessageValidity) = useState { true }
	 *
	 * Column {
	 * 	Input(
	 * 		value = message,
	 * 		onChange = setMessage,
	 * 		required = true,
	 * 		valid = messageValid,
	 * 		onChangeValidity = setMessageValidity
	 * 	)
	 * }
	 * ```
	 */
	onChangeValidity: ((Boolean) -> Unit)? = null,

	/** [onOk] is invoked when pressing the keyboard Ok button.
	 * - See also: [okIcon] to customize the icon of this button.
	 * - See also: [hideKeyboardOnOk] to handle the keyboard closing before executing [onOk].
	 * ```
	 * Example usage:
	 * Input(
	 * 	value = message,
	 * 	onChange = setMessage,
	 * 	okIcon = ImeAction.Send,
	 * 	onOk = { sendMessage(message) }
	 * )
	 * ```
	 */
	onOk: (() -> Unit)? = null,

	/** [onFocusChange] is invoked when the focus state changes. */
	onFocusChange: ((Boolean) -> Unit)? = null,
) {
	val (passwordVisible, setPasswordVisibility) = useState { false }
	val (focus) = useState { focusRequester ?: FocusRequester() }
	val keyboardController = LocalSoftwareKeyboardController.current

	var modifier = Modifier
		.focusRequester(focus)
		.fillMaxWidth()
		.height(inputHeight)
		.background(Color.Transparent)
		.clip(RoundedCornerShape(radius))
		.background(Color.White)
	if (onFocusChange != null)
		modifier = modifier.onFocusChanged { onFocusChange(it.isFocused) }

	val onPressOk: (KeyboardActionScope.() -> Unit) = {
		focusNext?.requestFocus()
		if (onOk != null) {
			if (hideKeyboardOnOk) keyboardController?.hide()
			onOk()
		}
	}

	Column {
		if (label != null) {
			Row(
				modifier = Modifier
					.padding(bottom = inputLabelGap)
					.clickable { focus.requestFocus() }) {
				Text(text = label)
				if (required) Text(text = " *", color = MaterialTheme.colorScheme.error)
			}
		}

		OutlinedTextField(
			// Value Handling
			value = value,
			onValueChange = {
				if (required) onChangeValidity?.invoke(it != "")
				onChange(it)
			},
			placeholder = {
				if (placeholder != null || label != null)
					Text(
						text = placeholder ?: label!!,
						color = LocalContentColor.current
					)
			},

			// Styling
			modifier = modifier,
			shape = RoundedCornerShape(radius),
			colors = OutlinedTextFieldDefaults.colors(
				unfocusedTextColor = textColor,
				focusedTextColor = textColor,
				unfocusedBorderColor = if (valid) inputGray else MaterialTheme.colorScheme.error,
				focusedBorderColor = if (valid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
				cursorColor = MaterialTheme.colorScheme.primary,
				unfocusedPlaceholderColor = inputGray,
				focusedPlaceholderColor = inputGray,
			),

			// Statue & Lines
			enabled = true,
			readOnly = readOnly,
			singleLine = true,
			minLines = 1,
			maxLines = 1,

			// Icons & Password Visibility
			leadingIcon = if (icon != null) ({ Icon(id = icon) }) else null,
			trailingIcon = if (!password) trailingIcon else ({
				Icon(
					id = if (passwordVisible) R.drawable.icon_line_confirm else R.drawable.icon_close
				) {
					setPasswordVisibility(!passwordVisible)
				}
			}),
			visualTransformation = if (password && !passwordVisible) {
				PasswordVisualTransformation()
			} else {
				VisualTransformation.None
			},

			// Keyboard Options
			keyboardOptions = KeyboardOptions(
				keyboardType = if (password) KeyboardType.Password else keyboardType,
				imeAction = if (focusNext != null) ImeAction.Next else okIcon,
			),
			keyboardActions = KeyboardActions(
				onDone = onPressOk,
				onGo = onPressOk,
				onNext = onPressOk,
				onPrevious = onPressOk,
				onSearch = onPressOk,
				onSend = onPressOk,
			),
		)
	}
}

@Composable
fun Input(
	field: Field,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's null, it takes the label value if present. */
	@StringRes placeholder: Int? = null,

	/** [readOnly] indicates if the field should accept any changes by the user interaction. */
	readOnly: Boolean = false,

	/** [password] indicates if the field is a password or not. If `true`, a custom hide/show icons with their logic are applied. */
	password: Boolean = false,

	/** [icon] is the resource ID of the prefix icon.
	 * ```
	 * Example usage:
	 * Input(placeholder = "Search", icon = R.drawable.icon_search)
	 * ```
	 */
	icon: Int? = null,

	trailingIcon: @Composable (() -> Unit)? = null,

	/** [okIcon] is the icon type of the keyboard OK button, e.g. `ImeAction.Done`, `ImeAction.Next`, `ImeAction.Send`
	 * - See also [onOk] to set a custom behavior when clicking on this button
	 */
	okIcon: ImeAction = ImeAction.Default,

	/** If the [onOk] callback is present, by default the keyboard will be hidden before executing it.
	 * [hideKeyboardOnOk] is to control the keyboard to stay visible or not.
	 */
	hideKeyboardOnOk: Boolean = true,

	/** [keyboardType] is the keyboard type, e.g. `KeyboardType.Text`, `KeyboardType.Email`, `KeyboardType.Decimal`
	 * - Note: It has no effect if [password] is `true`, as `KeyboardType.Password` is applied automatically.
	 */
	keyboardType: KeyboardType = KeyboardType.Text,

	/** When you have multiple fields in a page that you expect from the user to fill the sequence.
	 * Give them the option of focusing on the next field from the keyboard for better UX.
	 * ```
	 * Example usage:
	 *
	 * Column {
	 * 	val (secondFieldFocus) = useState { FocusRequester() }
	 *
	 * 	Input(focusNext = secondFieldFocus)
	 * 	Input(focusRequester = secondFieldFocus)
	 * }
	 * ```
	 */
	focusNext: FocusRequester? = null,

	// Events

	/** [onOk] is invoked when pressing the keyboard Ok button.
	 * - See also: [okIcon] to customize the icon of this button.
	 * - See also: [hideKeyboardOnOk] to handle the keyboard closing before executing [onOk].
	 * ```
	 * Example usage:
	 * Input(
	 * 	value = message,
	 * 	onChange = setMessage,
	 * 	okIcon = ImeAction.Send,
	 * 	onOk = { sendMessage(message) }
	 * )
	 * ```
	 */
	onOk: (() -> Unit)? = null,

	/** [onFocusChange] is invoked when the focus state changes. */
	onFocusChange: ((Boolean) -> Unit)? = null,
) {
	Input(
		value = field.value,
		label = label,
		placeholder = placeholder,
		required = field.required,
		valid = field.valid,
		readOnly = readOnly,
		password = password,
		icon = icon,
		trailingIcon = trailingIcon,
		okIcon = okIcon,
		hideKeyboardOnOk = hideKeyboardOnOk,
		keyboardType = keyboardType,
		focusRequester = field.focusRequester,
		focusNext = focusNext,
		onChange = { field.value = it },
		onChangeValidity = { field.valid = it },
		onOk = onOk,
		onFocusChange = onFocusChange,
	)
}
