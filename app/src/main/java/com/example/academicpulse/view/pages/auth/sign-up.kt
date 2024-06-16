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
import com.example.academicpulse.model.SignInInfo
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store

@Composable
fun SignUpPage() {
	val auth = Store.auth
	val form = Form.use()
	val firstName = Field.use(
		form = form,
		value = auth.signUpInfo.firstName,
		regex = Form.name,
		ifEmpty = R.string.first_name_required,
		ifInvalid = R.string.first_name_invalid,
	)
	val lastName = Field.use(
		form = form,
		value = auth.signUpInfo.lastName,
		regex = Form.name,
		ifEmpty = R.string.last_name_required,
		ifInvalid = R.string.last_name_invalid,
	)
	val email = Field.use(
		form = form,
		value = auth.signUpInfo.email,
		regex = Form.email,
		ifEmpty = R.string.email_required,
		ifInvalid = R.string.email_invalid,
	)
	val password = Field.use(
		form = form,
		regex = Form.password,
		ifEmpty = R.string.password_required,
		ifInvalid = R.string.password_not_strong,
	)

	val (loading, setLoading) = useState { false }

	fun signUp() {
		if (loading || !form.validate()) return
		setLoading(true)
		auth.saveSignUpInfo(firstName.value, lastName.value, email.value)
		auth.signUp(SignInInfo(email.value, password.value)) { message ->
			setLoading(false)
			form.error = message
		}
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.sign_up)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(text = R.string.institution_info)
			Description(text = R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Input(
				field = firstName,
				label = R.string.first_name,
				focusNext = lastName,
			)
			Input(
				field = lastName,
				label = R.string.last_name,
				focusNext = email,
			)
			Input(
				field = email,
				label = R.string.institution_email,
				focusNext = password,
				keyboardType = KeyboardType.Email,
			)
			Input(
				field = password,
				label = R.string.password,
				placeholder = R.string.create_password,
				password = true,
				onOk = ::signUp,
			)
			form.Error()
		}

		Spacer(Modifier.weight(1f))
		Button(
			text = R.string.continued,
			modifier = Modifier.padding(bottom = 60.dp),
			loading = loading,
			onClick = ::signUp,
		)
	}

	BackHandler {
		if (loading) return@BackHandler
		auth.saveSignUpInfo(firstName.trim(), lastName.trim(), email.trim())
		Router.back(false /* to = auth/sign-up-institution */)
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpUserPage() {
	SignUpPage()
}
