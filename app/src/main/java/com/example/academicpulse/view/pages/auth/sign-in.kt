package com.example.academicpulse.view.pages.auth

import androidx.activity.compose.BackHandler
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
import com.example.academicpulse.model.Field
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@Composable
fun SignInPage() {
	val auth = Store.auth
	val form = useForm()
	val email = useField(
		form = form,
		value = auth.signInInfo.email,
		regex = Field.email,
		ifEmpty = R.string.email_required,
		ifInvalid = R.string.email_invalid,
	)
	val password = useField(
		form = form,
		value = auth.signInInfo.password,
		regex = Field.password,
		ifEmpty = R.string.password_required,
		ifInvalid = R.string.password_invalid,
	)

	val (loading, setLoading) = useState(false)

	fun signIn() {
		if (loading) return
		if (form.validate()) {
			setLoading(true)
			auth.saveSignInInfo(email.trim(), password.trim())
			auth.signIn { error ->
				setLoading(false)
				form.error = error
			}
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
					label = R.string.email_address,
					focusNext = password.focusRequester,
					keyboardType = KeyboardType.Email,
				)
				Input(
					field = password,
					label = R.string.password,
					placeholder = R.string.create_password,
					password = true,
					onOk = ::signIn,
				)
				form.Error()
				Button(
					text = R.string.sign_in,
					modifier = Modifier.padding(top = (if (form.valid) 14 else 3).dp),
					loading = loading,
					onClick = ::signIn,
				)
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
						Router.navigate("auth/sign-up-institution", false)
					}
				)
			}
		}
	}

	BackHandler {
		if (loading) return@BackHandler
		Router.exit()
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignInPage() {
	SignInPage()
}
