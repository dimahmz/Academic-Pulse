package com.example.academicpulse.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import com.example.academicpulse.model.Field
import com.example.academicpulse.model.Form

@Composable
fun useForm(): Form {
	return useState({ Form() }).first
}

@Composable
fun useField(
	form: Form,
	value: String?,
	required: Boolean = true,
	validator: ((Field) -> Boolean)? = null,
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

@Composable
fun useField(
	form: Form,
	value: String?,
	required: Boolean = true,
	regex: String? = null,
	@StringRes ifEmpty: Int? = null,
	@StringRes ifInvalid: Int? = null
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
