package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.inputGray
import com.example.academicpulse.theme.inputHeight
import com.example.academicpulse.theme.inputLabelGap
import com.example.academicpulse.theme.radius
import com.example.academicpulse.model.Field
import com.example.academicpulse.theme.white
import com.example.academicpulse.utils.useState

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
	val (isFocused, setFocusState) = useState { false }
	val (focus) = useState { focusRequester ?: FocusRequester() }
	val keyboardController = LocalSoftwareKeyboardController.current
	val (passwordVisible, setPasswordVisibility) = useState { false }

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

		BasicTextField(
			// Value Handling
			value = value,
			onValueChange = {
				if (required) onChangeValidity?.invoke(it != "")
				onChange(it)
			},

			modifier = Modifier
				.fillMaxWidth()
				.height(inputHeight)
				.background(color = white, shape = RoundedCornerShape(radius))
				.border(
					width = if (isFocused) 2.dp else 1.dp,
					color = if (isFocused) MaterialTheme.colorScheme.primary
					else if (!valid) MaterialTheme.colorScheme.error
					else inputGray,
					shape = RoundedCornerShape(radius)
				)
				.focusRequester(focus)
				.onFocusChanged { focusState ->
					setFocusState(focusState.isFocused)
					onFocusChange?.invoke(focusState.isFocused)
				},
			cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),

			// Statue & Lines
			enabled = true,
			readOnly = readOnly,
			singleLine = true,
			minLines = 1,
			maxLines = 1,

			// Password Visibility
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
		) { innerTextField ->
			// Input content: value, placeholder and icons
			Row(
				Modifier.padding(horizontal = 16.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (icon != null) {
					Icon(id = icon, color = Color.Black.copy(alpha = 0.6f))
					Spacer(Modifier.width(8.dp))
				}
				Box(Modifier.weight(1f)) {
					if (value.isEmpty() && (placeholder != null || label != null)) {
						val color = if (isFocused) inputGray.copy(alpha = 0.6f) else inputGray
						Text(text = placeholder ?: label!!, color = color)
					}
					innerTextField()
				}
				if (password) {
					val btn = if (passwordVisible) R.drawable.icon_line_confirm else R.drawable.icon_close
					Icon(id = btn, color = Color.Black.copy(alpha = 0.6f)) {
						setPasswordVisibility(!passwordVisible)
					}
				} else if (trailingIcon != null) trailingIcon()
				if (password || trailingIcon != null) Spacer(Modifier.width(8.dp))
			}
		}
	}
}
