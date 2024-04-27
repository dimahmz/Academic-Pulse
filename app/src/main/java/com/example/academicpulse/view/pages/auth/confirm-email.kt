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
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.confirmation_page_title, paddingBottom = 60.dp)

		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.spacedBy(40.dp)
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Title(text = R.string.confirmation_title)
				Description(text = R.string.confirmation_description)
			}
			Input(
				field = code,
				placeholder = Res.string(R.string.confirmation_title),
				keyboardType = KeyboardType.Number,
			)
			form.Error()
		}

		Spacer(Modifier.weight(1f))
		Column(
			modifier = Modifier.padding(bottom = 60.dp),
			verticalArrangement = Arrangement.spacedBy(25.dp)
		) {
			H1(
				text = R.string.resend_code,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier.fillMaxWidth(),
				align = TextAlign.Center,
			)
			Button(text = R.string.continued) { validate() }
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewConfirmEmailPage() {
	ConfirmEmailPage()
}
