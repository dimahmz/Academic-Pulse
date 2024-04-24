package com.example.academicpulse.view.components.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.academicpulse.R
import com.example.academicpulse.theme.inputGray
import com.example.academicpulse.theme.inputHeight
import com.example.academicpulse.theme.inputLabelGap
import com.example.academicpulse.theme.radius
import com.example.academicpulse.utils.useState

@Composable
fun Input(
	value: String = "",

	/** A supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	label: String? = null,

	/** A supporting text placed inside the field.
	 * - Note: If it's null, it takes the label value if present. */
	placeholder: String? = null,

	/** Pass `true` to add a red symbol (`*`) next to the [label] to indicate that the field is required.
	 * - Note: It has no effect if [label] is `null`.
	 * - See also [valid] and [onChangeValidity] to automatically handle the field clearing.
	 */
	required: Boolean = true,

	/** Control the component theme to switch between the main them and error them. */
	valid: Boolean = true,

	/** Pass `true` to apply the custom hide/show icons with their logic */
	password: Boolean = false,

	/** Pass `true` to make the field unmodifiable */
	readOnly: Boolean = false,

	/** The prefix icon resource ID.
	 * ```
	 * Example usage:
	 * Input(placeholder = "Search", icon = R.drawable.icon_search)
	 * ```
	 */
	icon: Int? = null,

	/** The icon type of the keyboard OK button, e.g. `ImeAction.Done`, `ImeAction.Next`, `ImeAction.Send`
	 * - See also [onOk] to set a custom behavior when clicking on this button
	 */
	okIcon: ImeAction = ImeAction.Default,

	/** The keyboard type, e.g. `KeyboardType.Text`, `KeyboardType.Email`, `KeyboardType.Decimal`
	 * - Note: It has no effect if [password] is `true`, as `KeyboardType.Password` is applied automatically.
	 */
	keyboardType: KeyboardType = KeyboardType.Password,

	/** Used if there is any external Element or script that can trigger focus on the input
	 * ```
	 * Example usage:
	 * val (focusRequester) = useState(FocusRequester())
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
	 * 	val (secondFieldFocus) = useState(FocusRequester())
	 *
	 * 	Input(focusNext = secondFieldFocus)
	 * 	Input(focusRequester = secondFieldFocus)
	 * }
	 * ```
	 */
	focusNext: FocusRequester? = null,

	// Events

	/** Emits when the user change the input value. (Used to update the passed value)
	 * ```
	 * Example usage:
	 * val (message, setMessage) = useState("")
	 *
	 * Column {
	 * 	Text("Current value is: $message")
	 * 	Input(value = message, onChange = setMessage)
	 * }
	 * ```
	 */
	onChange: ((String) -> Unit)? = null,

	/** The short way to change the field validity depending on the field value if it is an empty string.
	 *
	 * Note: It has no effect if [required] is `false`.
	 * ```
	 * Example usage:
	 * val (message, setMessage) = useState("")
	 * val (messageValid, setMessageValidity) = useState(true)
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

	/** Emits when pressing the keyboard Ok button.
	 * - See also: [okIcon] to customize the icon of this button.
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
	onOk: (() -> Any)? = null,
) {
	val (passwordVisible, setPasswordVisibility) = useState(false)
	val (focus) = useState(focusRequester ?: FocusRequester())

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
				if (!readOnly) {
					if (required) onChangeValidity?.invoke(it != "")
					onChange?.invoke(it)
				}
			},
			placeholder = {
				if (placeholder != null || label != null) Text(text = placeholder ?: label ?: "")
			},

			// Styling
			modifier = Modifier
				.focusRequester(focus)
				.fillMaxWidth()
				.height(inputHeight),
			shape = RoundedCornerShape(radius),
			colors = OutlinedTextFieldDefaults.colors(
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
			trailingIcon = if (!password) null else ({
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
				onDone = { onOk?.invoke() },
				onGo = { onOk?.invoke() },
				onNext = {
					focusNext?.requestFocus()
					onOk?.invoke()
				},
				onPrevious = { onOk?.invoke() },
				onSearch = { onOk?.invoke() },
				onSend = { onOk?.invoke() },
			),
		)
	}
}
