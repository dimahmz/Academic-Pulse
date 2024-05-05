package com.example.academicpulse.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import com.example.academicpulse.model.Field
import com.example.academicpulse.model.Form

/** Instantiate and remember a form. */
@Composable
fun useForm(): Form {
	return useState({ Form() }).first
}

/** Instantiate and remember a field with primitive options.
 * - Note: It is automatically added to the form fields list (Don't use form.addField).
 *
 * ```
 * Example usage:
 *
 * val form = useForm()
 * val email = useField(
 * 	form = form,
 * 	value = Store.auth.signInInfo.email,
 * 	regex = Field.email,
 * 	ifEmpty = R.string.email_required,
 * 	ifInvalid = R.string.email_invalid,
 * )
 * ```
 */
@Composable
fun useField(
	form: Form,
	value: String?,
	required: Boolean = true,
	regex: String? = null,
	@StringRes ifEmpty: Int? = null,
	@StringRes ifInvalid: Int? = null,
): Field {
	val validator = useState({
		if (regex != null && ifEmpty != null && ifInvalid != null)
			fun(field: Field): Boolean { return field.validator(regex, ifEmpty, ifInvalid) }
		else if (regex != null && ifInvalid != null)
			fun(field: Field): Boolean { return field.validator(regex, ifInvalid) }
		else if (ifEmpty != null)
			fun(field: Field): Boolean { return field.validator(ifEmpty) }
		else null
	})

	return useField(
		form = form,
		value = value,
		required = required,
		validator = validator.first,
	)
}

/** Instantiate and remember a field.
 * - Note: It is automatically added to the form fields list (Don't use form.addField).
 *
 * ```
 * Example usage:
 *
 * val form = useForm()
 * val email = useField(form = form, value = "") { self ->
 * 	val value = self.trim()
 * 	return@useField customCheck(value)
 * }
 * ```
 */
@Composable
fun useField(
	form: Form, value: String?, required: Boolean = true, validator: ((Field) -> Boolean)? = null,
): Field {
	return useState({
		Field(
			form = form,
			value = value ?: "",
			required = required,
			focusRequester = FocusRequester(),
			validator = validator,
		)
	}).first
}
