package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header

@Composable
fun SignUpUserPage() {
	val form = useForm()
	val firstName = useField(
		form = form,
		value = "",
	)
	val lastName = useField(
		form = form,
		value = "",
	)
	val email = useField(
		form = form,
		value = "",
	)
	val password = useField(
		form = form,
		value = "",
	)

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
			)
			Input(
				field = password,
				label = Res.string(R.string.password),
				placeholder = Res.string(R.string.create_password),
				password = true,
			)
			Text(text = form.error.value, color = MaterialTheme.colorScheme.error)
		}
		form.Error()

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
