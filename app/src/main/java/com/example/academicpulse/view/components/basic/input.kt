package com.example.academicpulse.view.components.basic

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
import com.example.academicpulse.theme.white
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.useState

@SuppressLint("UnnecessaryComposedModifier")
@Composable
fun Input(
	field: Field,

	renderedValue: String? = null,

	/** [label] is a supporting text placed above the field.
	 * - Note: It can automatically request a focus on the field when it's clicked. */
	@StringRes label: Int? = null,

	/** [placeholder] is a supporting text placed inside the field.
	 * - Note: If it's null, it takes the label value if present. */
	@StringRes placeholder: Int? = null,

	/** [outlined] indicates if the field has borders or not. */
	outlined: Boolean = true,

	/** [centered] indicates if the input content should be centered. */
	centered: Boolean = false,

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
	 * val form = Form.use()
	 * val field1 = Field.use(form = form)
	 * val field2 = Field.use(form = form)
	 *
	 * Input(field = field1, focusNext = field2)
	 * Input(field = field2)
	 * ```
	 */
	focusNext: Field? = null,

	// Events

	/** [onOk] is invoked when pressing the keyboard Ok button.
	 * - See also: [okIcon] to customize the icon of this button.
	 * - See also: [hideKeyboardOnOk] to handle the keyboard closing before executing [onOk].
	 * ```
	 * Example usage:
	 * val message = Field.use(form = null)
	 *
	 * Input(
	 * 	field = message,
	 * 	okIcon = ImeAction.Send,
	 * 	onOk = { sendMessage(message.value) }
	 * )
	 * ```
	 */
	onOk: (() -> Unit)? = null,
) {
	val (interactModifier, setInteractModifier) = useState<Modifier> { Modifier }
	LaunchedEffect(readOnly) {
		setInteractModifier(
			if (readOnly) Modifier
			else {
				val focusRequester = field.focusRequester
				if (focusRequester == null) Modifier.clickable { field.focus = true }
				else Modifier
					.focusRequester(focusRequester)
					.onFocusChanged { field.focusChanger(it.isFocused, viaInput = true) }
			}
		)
	}
	val (styleModifier, setStyleModifier) = useState<Modifier> { Modifier }
	val colors = MaterialTheme.colorScheme
	LaunchedEffect("$outlined ${field.valid} ${field.focus}") {
		setStyleModifier(
			if (outlined)
				Modifier
					.border(
						width = if (field.focus) 2.dp else 1.dp,
						shape = RoundedCornerShape(radius),
						color = if (field.focus) colors.primary else if (!field.valid) colors.error else inputGray,
					)
					.background(color = white, shape = RoundedCornerShape(radius))
			else Modifier.background(color = white)
		)
	}

	val modified = Modifier
		.fillMaxWidth()
		.height(inputHeight)
		.composed(factory = { interactModifier })
		.composed { styleModifier }

	Column {
		if (label != null) {
			Row(
				modifier = Modifier
					.padding(bottom = inputLabelGap)
					.clickable { field.focus = true }
			) {
				Text(text = label)
				if (field.required) Text(text = " *", color = MaterialTheme.colorScheme.error)
			}
		}

		if (field.focusRequester == null) {
			Row(modifier = modified) {
				FieldContent(
					modifier = Modifier.weight(1f),
					value = renderedValue ?: field.value,
					label = label,
					placeholder = placeholder,
					outlined = outlined,
					isFocused = field.focus,
					centered = centered,
					icon = icon,
					trailingIcon = trailingIcon,
				) {
					if (field.value.isNotBlank()) Text(text = renderedValue ?: field.value)
				}
			}
		} else {
			val keyboardController = LocalSoftwareKeyboardController.current
			val (passwordVisible, setPasswordVisibility) = useState { false }
			val onPressOk: (KeyboardActionScope.() -> Unit) = {
				if (focusNext != null) focusNext.focus = true
				if (onOk != null) {
					if (hideKeyboardOnOk) keyboardController?.hide()
					onOk()
				}
			}

			BasicTextField(
				modifier = modified,
				cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),

				// Value Handling
				value = renderedValue ?: field.value,
				onValueChange = { field.value = it },

				// Statue & Lines
				enabled = true,
				readOnly = readOnly,
				singleLine = true,
				minLines = 1,
				maxLines = 1,

				// Password Visibility
				visualTransformation =
				if (password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,

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
				FieldContent(
					value = renderedValue ?: field.value,
					label = label,
					placeholder = placeholder,
					outlined = outlined,
					isFocused = field.focus,
					centered = centered,
					content = innerTextField,
					icon = icon,
					trailingIcon = if (!password) trailingIcon
					else {
						{
							val btn = if (passwordVisible) R.drawable.icon_line_confirm else R.drawable.icon_close
							Icon(id = btn, color = Color.Black.copy(alpha = 0.5f)) {
								setPasswordVisibility(!passwordVisible)
							}
						}
					},
				)
			}
		}
	}
}

@Composable
private fun FieldContent(
	modifier: Modifier = Modifier,
	value: String,
	label: Int?,
	placeholder: Int?,
	outlined: Boolean,
	isFocused: Boolean,
	centered: Boolean,
	icon: Int?,
	trailingIcon: @Composable (() -> Unit)? = null,
	content: @Composable () -> Unit,
) {
	val (iconOpacity, setIconOpacity) = useState { 0.3f }
	LaunchedEffect(value.isBlank()) {
		setIconOpacity(if (value.isBlank()) 0.3f else 0.5f)
	}

	Row(
		modifier
			.height(inputHeight)
			.padding(horizontal = if (outlined) 16.dp else 0.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
	) {
		if (icon != null)
			Box(modifier = Modifier.padding(end = 8.dp)) {
				Icon(id = icon, color = Color.Black.copy(alpha = iconOpacity))
			}
		var inlineModifier = Modifier.height((inputHeight.value - 6).dp)
		inlineModifier = if (centered) inlineModifier else inlineModifier.weight(1f)
		Box(modifier = inlineModifier) {
			if (value.isBlank() && (placeholder != null || label != null)) {
				Row(inlineModifier, verticalAlignment = Alignment.CenterVertically) {
					val color = if (isFocused) inputGray.copy(alpha = 0.6f) else inputGray
					Text(text = placeholder ?: label!!, color = color)
				}
			}
			Row(inlineModifier, verticalAlignment = Alignment.CenterVertically, content = { content() })
		}
		if (trailingIcon != null)
			Box(modifier = Modifier.padding(start = 8.dp), content = { trailingIcon() })
	}
}
