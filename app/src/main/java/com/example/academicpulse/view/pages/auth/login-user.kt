package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@Composable
fun LogInUserPage() {
	val auth = Store.auth()
	val form = useForm()
	val email = useField(
		form = form,
		value = auth.signUpInfo.email,
		regex = Form.email,
		ifEmpty = "The email is required.",
		ifInvalid = "The email is invalid."
	)
	val password = useField(
		form = form,
		value = auth.signUpInfo.password,
		regex = Form.password,
		ifEmpty = "The password is required.",
		ifInvalid = "The password is invalid."
	)

	fun validate() {
		if (form.validate()) {
			auth.saveLoginInfo(email.trim(), password.trim())
			Router.navigate("auth/activate-account", false)
		} else form.focusOnFirstInvalidField()
	}

	Column(modifier = Modifier.fillMaxHeight()) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(240.dp)
				.background(MaterialTheme.colorScheme.secondary),
			contentAlignment = Alignment.Center,
		) {
			Image(id = R.drawable.app_logo, modifier = Modifier.size(140.dp))
		}

		Column(
			modifier = Modifier
				.padding(horizontal = pagePaddingX)
				.weight(1f)
		) {
			H1(
				text = R.string.welcome,
				modifier = Modifier.padding(vertical = 26.dp),
			)

			Column(verticalArrangement = Arrangement.spacedBy(gap)) {
				Input(
					field = email,
					label = Res.string(R.string.email_address),
					focusNext = password.focusRequester,
					keyboardType = KeyboardType.Email,
				)
				Input(
					field = password,
					label = Res.string(R.string.password),
					placeholder = Res.string(R.string.create_password),
					password = true,
				)
				form.Error()
				Button(
					text = R.string.login,
					modifier = Modifier.padding(top = (if (form.valid()) 14 else 3).dp),
				) { validate() }
			}

			Row(
				modifier = Modifier
					.padding(top = 20.dp)
					.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			) {
				Text(text = R.string.new_member, modifier = Modifier.padding(end = 10.dp))
				Text(
					text = R.string.register_now,
					color = MaterialTheme.colorScheme.primary,
					weight = FontWeight.Bold,
					modifier = Modifier.clickable {
						auth.clearLogin()
						Router.navigate("auth/sign-up-institution", false)
					}
				)
			}
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLogInUserPage() {
	LogInUserPage()
}
