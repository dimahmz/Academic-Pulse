package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header

@Composable
fun ConfirmEmailPage() {
	val form = useForm()
	val code = useField(
		form = form,
		value = "",
		regex = "^[0-9]{4}\$",
		ifEmpty = Res.string(R.string.confirmation_error),
		ifInvalid = Res.string(R.string.confirmation_error),
	)

	fun validate() {
		if (form.validate()) {
			Router.navigate("auth/verified-email", false)
		} else form.focusOnFirstInvalidField()
	}

	Column(
		modifier = Modifier
			.padding(horizontal = pagePaddingX)
			.fillMaxHeight()
	) {
		Header(title = R.string.confirmation_page_title)

		Column(
			modifier = Modifier
				.padding(bottom = 30.dp, top = 60.dp)
				.fillMaxWidth(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Title(text = R.string.confirmation_title)
			Spacer(Modifier.padding(vertical = 4.dp))
			Description(text = R.string.confirmation_description)
			Spacer(Modifier.padding(vertical = 20.dp))
			Input(
				field = code,
				placeholder = Res.string(R.string.confirmation_title),
				keyboardType = KeyboardType.Number,
			)
			form.Error(40)
		}

		Spacer(Modifier.weight(1f))
		H1(
			modifier = Modifier
				.padding(vertical = 25.dp)
				.fillMaxWidth(),
			text = R.string.resend_code,
			color = MaterialTheme.colorScheme.primary,
			align = TextAlign.Center
		)
		Button(text = R.string.continued, modifier = Modifier.padding(bottom = 60.dp)) { validate() }
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewConfirmEmailPage() {
	ConfirmEmailPage()
}
