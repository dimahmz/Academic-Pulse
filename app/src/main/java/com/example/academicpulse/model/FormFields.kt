package com.example.academicpulse.model

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useField
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Text

/** This class is used in the context of form fields, to group some [Input] props and facilitate handling errors.
 * - Note: use [useField] to instantiate fields.
 */
class Field(
	value: String,
	val required: Boolean,
	val focusRequester: FocusRequester,
	val form: Form,
	val validator: ((Field) -> Boolean)? = null,
) {
	private val _value: MutableState<String> = mutableStateOf(value)
	private val _valid: MutableState<Boolean> = mutableStateOf(true)

	init {
		form.addField(this)
	}

	// Accessors
	var value: String
		get() = _value.value
		set(value) {
			this._value.value = value
		}
	var valid: Boolean
		get() = _valid.value
		set(value) {
			this._valid.value = value
		}

	/** Returns a string having leading and trailing whitespace removed after modifying the field value if it includes them. */
	fun trim(): String {
		val fieldValue = value.trim()
		if (fieldValue != value) value = fieldValue
		return fieldValue
	}

	/** A pre-defined validator: Use this if the field is required and it should follow certain pattern. */
	fun validator(regex: String, @StringRes ifEmpty: Int, @StringRes ifInvalid: Int): Boolean {
		val value = trim()
		valid = true
		if (required && value == "") {
			valid = false
			form.error = ifEmpty
		} else if (!Regex(regex).matches(value)) {
			valid = false
			form.error = ifInvalid
		}
		return valid
	}

	/** A pre-defined validator: Use this if the field is only required. */
	fun validator(@StringRes ifEmpty: Int): Boolean {
		val value = trim()
		valid = true
		if (required && value == "") {
			valid = false
			form.error = ifEmpty
		}
		return valid
	}

	/** A pre-defined validator: Use this if the field is not required but it should follow certain pattern. */
	fun validator(regex: String, @StringRes ifInvalid: Int): Boolean {
		val value = trim()
		valid = true
		if (!Regex(regex).matches(value)) {
			valid = false
			form.error = ifInvalid
		}
		return valid
	}

	companion object {
		/** A regex of names in french, (e.g., usernames, publications names, ...etc). use it with [useField] */
		const val name = "^[\\sA-Za-zÀ-ÖÙ-Ýà-öù-ýĀ-ž']*\$"

		/** A regex of emails. use it with [useField] */
		const val email =
			"^\\s*(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\\s*\$"

		/** A regex of strong passwords, (It can be used also for validity). use it with [useField] */
		const val password = "^.{8,}$"
	}
}

/** This class is used in the context of forms, to hold a group of [Field]s and validate them as a one piece.
 * - Note: use [useForm] to instantiate forms.
 */
class Form {
	private val fields: MutableList<Field> = mutableListOf()
	private val _error: MutableState<Int?> = mutableStateOf(null)
	private val _valid: MutableState<Boolean> = mutableStateOf(true)

	// Accessors
	val valid
		get() = _valid.value
	var error: Int?
		get() = _error.value
		set(@StringRes value) {
			this._error.value = value
			this._valid.value = value == null
		}

	/** Used to insert the field into the list
	 *
	 * Note:
	 * - The fields order is important for errors visibility priorities.
	 * - For safe usage, use [useField] instead.
	 */
	fun addField(field: Field) {
		if (!fields.contains(field)) fields.add(field)
	}

	/** Validate all the fields that exist in the form. */
	fun validate(): Boolean {
		error = null
		_valid.value = true
		for (field in fields.reversed()) {
			val isValid = field.validator?.invoke(field) ?: true
			if (field.valid != isValid) field.valid = isValid
			if (valid && !isValid) _valid.value = false
		}
		return valid
	}

	/** After validating the form, you can focus on some fields to let the user start typing easily.
	 * ```
	 * Example usage:
	 *
	 * if (form.validate()) {
	 * 	auth.signIn(email.trim(), password.trim()) { error ->
	 * 		form.error = error
	 * 	}
	 * } else form.focusOnFirstInvalidField()
	 * ```
	 */
	fun focusOnFirstInvalidField() {
		for (field in fields) {
			if (!field.valid) {
				field.focusRequester.requestFocus()
				break
			}
		}
	}

	/** A composable UI element that hold the form error if exist.
	 * - Note: The form error can be the error of the first invalid field or you can set it manually using the **error** setter.
	 */
	@Composable
	fun Error() {
		if (!valid && error != null) {
			Text(text = error!!, color = MaterialTheme.colorScheme.error)
		}
	}
}
