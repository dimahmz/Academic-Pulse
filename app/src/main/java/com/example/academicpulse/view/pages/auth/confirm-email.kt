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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header

@Composable
fun ConfirmEmailPage() {
	val (optCode, setOptCode) = useState("")
	val (optCodeValid, setOptCodeValidity) = useState(true)
	val (showErrorMsg, setShowErrorMsg) = useState(false);
	val (isOptCodeCorrect, setIsOptCodeCorrectness) = useState(false);

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
				value = optCode,
				onChange = setOptCode,
				valid = optCodeValid,
				onChangeValidity = setOptCodeValidity,
			)
			Spacer(Modifier.padding(vertical = 20.dp))
			// confirmation_error
			if (showErrorMsg)
				Title(text = R.string.confirmation_error, color = MaterialTheme.colorScheme.error)
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
		Button(text = R.string.continued, modifier = Modifier.padding(bottom = 80.dp)) {
			Router.navigate("auth/verified-email", false)
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewConfirmEmailPage() {
	ConfirmEmailPage()
}
