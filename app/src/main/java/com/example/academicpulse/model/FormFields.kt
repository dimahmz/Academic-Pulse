package com.example.academicpulse.model

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Text

/** This class is used in the context of form fields, to group some [Input] props and facilitate handling errors. */
class Field(
	value: String,
	val required: Boolean,
	val focusRequester: FocusRequester,
	private val error: MutableState<String>,
	private val validator: ((Field) -> Boolean)?,
	private val valid: MutableState<Boolean> = mutableStateOf(true)
) {
	private val value: MutableState<String> = mutableStateOf(value)

	fun value(): String {
		return this.value.value
	}

	fun value(value: String) {
		this.value.value = value
	}

	fun valid(): Boolean {
		return this.valid.value
	}

	fun valid(valid: Boolean) {
		this.valid.value = valid
	}

	/** USE ONLY FOR CHECKING: Use this for executing the validator declared when creating the Field instance */
	fun validate(): Boolean {
		return validator?.invoke(this) ?: true
	}

	/** USE ONLY IN DECLARATION: Use this if the field is required and it should follow certain pattern. */
	fun validator(regex: String, ifEmpty: String, ifInvalid: String): Boolean {
		val value = this.value()
		valid(true)
		if (required && value == "") {
			valid(false)
			error.value = ifEmpty
		} else if (!Regex(regex).matches(value)) {
			valid(false)
			error.value = ifInvalid
		}
		return valid()
	}

	/** USE ONLY IN DECLARATION: Use this if the field is only required. */
	fun validator(ifEmpty: String): Boolean {
		val value = this.value()
		valid(true)
		if (required && value == "") {
			valid(false)
			error.value = ifEmpty
		}
		return valid()
	}

	/** USE ONLY IN DECLARATION: Use this if the field is not required but it should follow certain pattern. */
	fun validator(regex: String, ifInvalid: String): Boolean {
		val value = this.value()
		valid(true)
		if (!Regex(regex).matches(value)) {
			valid(false)
			error.value = ifInvalid
		}
		return valid()
	}
}

/** This class is used in the context of forms, to hold a group of [Field]s and validate them as a one piece. */
class Form {
	val error: MutableState<String> = mutableStateOf("")
	private val valid: MutableState<Boolean> = mutableStateOf(true)
	private val fields: MutableList<Field> = mutableListOf()

	fun addField(field: Field) {
		fields.add(field)
	}

	fun validate() {
		valid.value = true
		error.value = ""
		for (field in fields.reversed()) {
			val isValid = field.validate()
			if (isValid != valid.value) valid.value = valid.value
		}
	}

	@Composable
	fun Error(paddingTop: Int = 0) {
		if (!valid.value) {
			Text(
				text = error.value,
				color = MaterialTheme.colorScheme.error,
				modifier = Modifier.padding(top = paddingTop.dp)
			)
		}
	}
}
