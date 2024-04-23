package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.material.*
import com.example.academicpulse.view_model.AuthViewModel

@Composable
fun SignUpUserPage(viewModel: AuthViewModel) {
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
		Header(R.string.signup)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(R.string.institution_info)
			Description(R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Input(
				value = firstName,
				onChange = setFirstName,
				label = Res.string(R.string.first_name),
				required = true,
				valid = firstNameValid,
				onChangeValidity = setFirstNameValidity,
			)
			Input(
				value = lastName,
				onChange = setLastName,
				label = Res.string(R.string.last_name),
				required = true,
				valid = lastNameValid,
				onChangeValidity = setLastNameValidity,
			)
			Input(
				value = email,
				onChange = setEmail,
				label = Res.string(R.string.institution_email),
				required = true,
				valid = emailValid,
				onChangeValidity = setEmailValidity,
			)
			Input(
				value = password,
				onChange = setPassword,
				label = Res.string(R.string.password),
				placeholder = Res.string(R.string.create_password),
				required = true,
				valid = passwordValid,
				onChangeValidity = setPasswordValidity,
			)
		}

		Spacer(Modifier.weight(1f))
		Button(text = R.string.continued, modifier = Modifier.padding(bottom = 80.dp))
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpUserPage() {
	SignUpUserPage(AuthViewModel())
}
