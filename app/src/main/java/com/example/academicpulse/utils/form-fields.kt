package com.example.academicpulse.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import com.example.academicpulse.model.Field
import com.example.academicpulse.model.Form

@Composable
fun useForm(): Form {
	return useState({ Form() }).first
}

@Composable
fun useField(
	form: Form? = null,
	value: String?,
	required: Boolean = true,
	validator: ((Field) -> Boolean)? = null,
): Field {
	return useState({
		val field = Field(
			value = value ?: "",
			required = required,
			error = form?.error ?: mutableStateOf(""),
			focusRequester = FocusRequester(),
			validator = validator,
		)
		form?.addField(field)
		field
	}).first
}

@Composable
fun useField(
	form: Form? = null,
	value: String?,
	required: Boolean = true,
	regex: String? = null,
	ifEmpty: String? = null,
	ifInvalid: String? = null
): Field {
	return useState({
		val validator = if (regex != null && ifEmpty != null && ifInvalid != null)
			fun(field: Field): Boolean { return field.validator(regex, ifEmpty, ifInvalid) }
		else if (regex != null && ifInvalid != null)
			fun(field: Field): Boolean { return field.validator(regex, ifInvalid) }
		else if (ifEmpty != null)
			fun(field: Field): Boolean { return field.validator(ifEmpty) }
		else null

		val field = Field(
			value = value ?: "",
			required = required,
			error = form?.error ?: mutableStateOf(""),
			focusRequester = FocusRequester(),
			validator = validator,
		)
		form?.addField(field)
		field
	}).first
}
