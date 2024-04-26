package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.*

@Composable
fun VerifiedEmailPage() {
	Column(
		modifier = Modifier
			.padding(horizontal = pagePaddingX)
			.fillMaxHeight(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Column(modifier = Modifier.padding(bottom = 30.dp, top = 60.dp)) {
			Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
				Image(id = R.drawable.icon_success_circle, modifier = Modifier.size(70.dp))
			}

			H1(
				text = R.string.email_verified,
				align = TextAlign.Center,
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp)
			)
			Description(
				modifier = Modifier.fillMaxWidth(),
				text = R.string.email_verified_description,
				align = TextAlign.Center
			)
		}

		Button(text = R.string.back_login, modifier = Modifier.padding(bottom = 80.dp)) {
			Router.back(/* to = auth/sign-up-user */)
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewVerifiedEmailPage() {
	VerifiedEmailPage()
}
