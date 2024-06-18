package com.example.academicpulse.utils.forms

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Input

/** This class is used in the context of form fields, to group some [Input] props and facilitate handling errors.
 *
 * **Note:**
 * 	- use [Field.use] to instantiate fields inside composable functions.
 * 	- use [Field.simple] to instantiate fields in other contexts like ViewModel.
 */
class Field private constructor(
	val form: Form?,
	value: String,
	val required: Boolean,
	private val regex: String? = null,
	@StringRes private val ifEmpty: Int? = null,
	@StringRes private val ifInvalid: Int? = null,
) {
	private val _value = mutableStateOf(value)
	private val _valid = mutableStateOf(true)
	private val _focus = mutableStateOf(false)
	private var _focusRequester: FocusRequester? = FocusRequester()
	private var _onFocus: ((Boolean) -> Unit)? = null
	private val _uri = mutableStateOf<Uri?>(null)

	// Accessors
	var value: String
		get() = _value.value
		set(value) {
			if (required) this.valid = (value != "")
			_value.value = value
		}
	var valid: Boolean
		get() = _valid.value
		set(value) {
			_valid.value = value
		}
	var focus: Boolean
		get() = _focus.value
		set(state) {
			focusChanger(state, false)
		}
	val focusRequester: FocusRequester?
		get() = _focusRequester
	var uri: Uri?
		get() = _uri.value
		set(value) {
			_uri.value = value
		}

	fun focusChanger(state: Boolean, viaInput: Boolean) {
		val oldState = _focus.value
		if (state && !oldState) {
			form?.fields?.forEach {
				if (it != this && it._focus.value) {
					it._focus.value = false
					it._onFocus?.invoke(false)
				}
			}
			_focus.value = true
			_onFocus?.invoke(true)
			if (!viaInput) _focusRequester?.requestFocus()
		} else if (!state && oldState) {
			_focus.value = false
			_onFocus?.invoke(false)
		}
	}

	/** Returns a string having leading and trailing whitespace removed after modifying the field value if it includes them. */
	fun trim(): String {
		val fieldValue = value.trim()
		if (fieldValue != value) value = fieldValue
		return fieldValue
	}

	fun clear() {
		_value.value = ""
		_valid.value = true
		_focus.value = false
		_uri.value = null
	}

	fun onFocusChange(callback: (Boolean) -> Unit) {
		if (_focusRequester != null) _focusRequester = null
		_onFocus = callback
	}

	companion object {
		/** Instantiate and remember a field.
		 * - Note: It is automatically added to the form fields list (Don't use form.addField).
		 *
		 * ```
		 * Example usage:
		 *
		 * val form = Form.use()
		 * val email = Field.use(
		 * 	form = form,
		 * 	value = Store.auth.signInInfo.email,
		 * 	regex = Field.email,
		 * 	ifEmpty = R.string.email_required,
		 * 	ifInvalid = R.string.email_invalid,
		 * )
		 * ```
		 */
		@Composable
		fun use(
			form: Form?,
			value: String? = "",
			required: Boolean = true,
			regex: String? = null,
			@StringRes ifEmpty: Int? = null,
			@StringRes ifInvalid: Int? = null,
		): Field {
			return useState { simple(form, value, required, regex, ifEmpty, ifInvalid) }.first
		}

		/** Instantiate without remembering a field.
		 * - Note: It is automatically added to the form fields list (Don't use form.addField).
		 *
		 * ```
		 * Example usage:
		 *
		 * class MyViewModel: ViewModel() {
		 * 	val form = Form.simple()
		 *
		 * 	val email = Field.simple(
		 * 		form = form,
		 * 		value = Store.auth.signInInfo.email,
		 * 		regex = Field.email,
		 * 		ifEmpty = R.string.email_required,
		 * 		ifInvalid = R.string.email_invalid,
		 * 	)
		 *
		 * 	fun clearForm() {
		 * 		form.clearAll()
		 * 	}
		 * }
		 * ```
		 */
		fun simple(
			form: Form?,
			value: String? = "",
			required: Boolean = true,
			regex: String? = null,
			@StringRes ifEmpty: Int? = null,
			@StringRes ifInvalid: Int? = null,
		): Field {
			val field = Field(form, value = value ?: "", required, regex, ifEmpty, ifInvalid)
			form?.addField(field)
			return field
		}

		fun validate(field: Field): Boolean {
			val value = field.trim()
			field.valid = true
			if (field.required && value == "") {
				field.valid = false
				if (field.ifEmpty != null) field.form?.error = field.ifEmpty
				else if (field.ifInvalid != null) field.form?.error = field.ifInvalid
			} else if (field.regex != null && !Regex(field.regex).matches(value)) {
				field.valid = false
				if (field.ifInvalid != null) field.form?.error = field.ifInvalid
			}
			return field.valid
		}
	}
}
