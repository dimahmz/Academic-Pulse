package com.example.academicpulse.utils.forms

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Text

/** This class is used in the context of forms, to hold a group of [Field]s and validate them as a one piece.
 *
 * **Note:**
 * 	- use [Form.use] to instantiate forms inside composable functions.
 * 	- use [Form.simple] to instantiate forms in other contexts like ViewModel.
 */
class Form private constructor() {
	private val _fields: MutableList<Field> = mutableListOf()
	private val _error: MutableState<Int?> = mutableStateOf(null)
	private val _valid: MutableState<Boolean> = mutableStateOf(true)

	// Accessors
	val fields: List<Field>
		get() = _fields
	val valid: Boolean
		get() = _valid.value
	var error: Int?
		get() = _error.value
		set(@StringRes value) {
			this._error.value = value
			this._valid.value = value == null
		}

	/** Don't use this method, it is already implemented in the [Field] constructor)
	 * - Note: The fields order is important for errors visibility priorities.
	 */
	fun addField(field: Field) {
		if (!_fields.contains(field)) _fields.add(field)
	}

	/** Validate all the fields that exist in the form. */
	fun validate(focusOnFirstInvalidField: Boolean = true): Boolean {
		error = null
		_valid.value = true
		_fields.reversed().forEach { field ->
			val isValid = Field.validate(field)
			if (field.valid != isValid) field.valid = isValid
			if (valid && !isValid) _valid.value = false
		}
		if (focusOnFirstInvalidField && !valid)
			for (field in _fields) {
				if (!field.valid) {
					field.focus = true
					break
				}
			}
		return valid
	}

	fun clearAll() {
		_fields.forEach { field -> field.clear() }
	}

	/** A composable UI element that hold the form error if exist.
	 *
	 * Note:
	 * - Use it directly without any if-else condition.
	 * - The form error can be the error of the first invalid field or you can set it manually using the **error** setter.
	 */
	@Composable
	fun Error() {
		val err = error
		if (!valid && err != null) Text(text = err, color = MaterialTheme.colorScheme.error)
	}

	companion object {
		/** A regex of names in french, (e.g., usernames, publications names, ...etc). use it with [Field.use] */
		const val name = "^[\\sA-Za-zÀ-ÖÙ-Ýà-öù-ýĀ-ž']*\$"

		/** A regex of emails. use it with [Field.use] */
		const val email =
			"^\\s*(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\\s*\$"

		/** A regex of strong passwords, (It can be used also for validity). use it with [Field.use] */
		const val password = "^.{8,}$"

		/** Instantiate and remember a form. */
		@Composable
		fun use(): Form {
			return useState { Form() }.first
		}

		/** Instantiate without remembering a form. */
		@Composable
		fun simple(): Form {
			return Form()
		}
	}
}
