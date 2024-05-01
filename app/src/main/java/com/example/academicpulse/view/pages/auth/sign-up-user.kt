package com.example.academicpulse.view.pages.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Form
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store

@Composable
fun SignUpUserPage() {
	val auth = Store.auth()
	val form = useForm()
	val firstName = useField(
		form = form,
		value = auth.signUpInfo.firstName,
		regex = Form.name,
		ifEmpty = "The first name is required.",
		ifInvalid = "The first name is invalid."
	)
	val lastName = useField(
		form = form,
		value = auth.signUpInfo.lastName,
		regex = Form.name,
		ifEmpty = "The last name is required.",
		ifInvalid = "The last name is invalid."
	)
	val email = useField(
		form = form,
		value = auth.signUpInfo.email,
		regex = Form.email,
		ifEmpty = "The email is required.",
		ifInvalid = "The email must be a valid email."
	)
	val password = useField(
		form = form,
		value = auth.signUpInfo.password,
		regex = Form.password,
		ifEmpty = "The password is required.",
		ifInvalid = "The password should be strong."
	)

	val (loading, setLoading) = useState(false)

	fun signup() {
		if (loading) return
		if (form.validate()) {
			setLoading(true)
			auth.saveSignUpInfo(firstName.trim(), lastName.trim(), email.trim(), password.trim())
			auth.signup { message ->
				setLoading(false)
				form.error(valid = false, error = message)
			}
		} else form.focusOnFirstInvalidField()
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.signup)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(text = R.string.institution_info)
			Description(text = R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Input(
				field = firstName,
				label = Res.string(R.string.first_name),
				focusNext = lastName.focusRequester,
			)
			Input(
				field = lastName,
				label = Res.string(R.string.last_name),
				focusNext = email.focusRequester,
			)
			Input(
				field = email,
				label = Res.string(R.string.institution_email),
				focusNext = password.focusRequester,
				keyboardType = KeyboardType.Email,
			)
			Input(
				field = password,
				label = Res.string(R.string.password),
				placeholder = Res.string(R.string.create_password),
				password = true,
				onOk = { signup() }
			)
			form.Error()
		}

		Spacer(Modifier.weight(1f))
		Button(
			text = R.string.continued,
			modifier = Modifier.padding(bottom = 60.dp),
			loading = loading,
		) { signup() }
	}

	BackHandler {
		if (loading) return@BackHandler
		auth.saveSignUpInfo(firstName.trim(), lastName.trim(), email.trim(), password.trim())
		Router.back(/* to = auth/sign-up-institution */)
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpUserPage() {
	SignUpUserPage()
}
