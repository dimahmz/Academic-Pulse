package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header

@Composable
fun SignUpUserPage() {
	val (firstName, setFirstName) = useState("")
	val (firstNameValid, setFirstNameValidity) = useState(true)
	val (lastName, setLastName) = useState("")
	val (lastNameValid, setLastNameValidity) = useState(true)
	val (email, setEmail) = useState("")
	val (emailValid, setEmailValidity) = useState(true)
	val (password, setPassword) = useState("")
	val (passwordValid, setPasswordValidity) = useState(true)

	Column(
		modifier = Modifier
			.padding(horizontal = pagePaddingX)
			.fillMaxHeight()
	) {
		Header(title = R.string.signup)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(text = R.string.institution_info)
			Description(text = R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			val (lastNameFocus) = useState(FocusRequester())
			val (emailFocus) = useState(FocusRequester())
			val (passwordFocus) = useState(FocusRequester())

			Input(
				value = firstName,
				onChange = setFirstName,
				label = Res.string(R.string.first_name),
				valid = firstNameValid,
				onChangeValidity = setFirstNameValidity,
				focusNext = lastNameFocus,
			)
			Input(
				value = lastName,
				onChange = setLastName,
				label = Res.string(R.string.last_name),
				valid = lastNameValid,
				onChangeValidity = setLastNameValidity,
				focusRequester = lastNameFocus,
				focusNext = emailFocus,
			)
			Input(
				value = email,
				onChange = setEmail,
				label = Res.string(R.string.institution_email),
				valid = emailValid,
				onChangeValidity = setEmailValidity,
				focusRequester = emailFocus,
				focusNext = passwordFocus,
			)
			Input(
				value = password,
				onChange = setPassword,
				label = Res.string(R.string.password),
				placeholder = Res.string(R.string.create_password),
				password = true,
				valid = passwordValid,
				onChangeValidity = setPasswordValidity,
				focusRequester = passwordFocus,
			)
		}

		Spacer(Modifier.weight(1f))
		Button(text = R.string.continued, modifier = Modifier.padding(bottom = 80.dp)) {
			Router.navigate("auth/confirm-email", false)
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpUserPage() {
	SignUpUserPage()
}
