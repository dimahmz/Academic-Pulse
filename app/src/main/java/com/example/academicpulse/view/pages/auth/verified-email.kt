package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@Composable
fun VerifiedEmailPage() {
	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
			Image(id = R.drawable.icon_success_circle, modifier = Modifier.size(70.dp))
		}

		Spacer(Modifier.height(20.dp))
		H1(text = R.string.email_verified, align = TextAlign.Center)
		Description(text = R.string.email_verified_description, align = TextAlign.Center)

		Column(
			modifier = Modifier.padding(vertical = 30.dp),
			verticalArrangement = Arrangement.spacedBy(gap)
		) {
			Button(text = R.string.continue_home) {
				Store.auth().clearLogin()
				Store.auth().clearSignUp()
				Router.replace("home", true)
			}
			Button(text = R.string.back_login, ghost = true) {
				Router.back(/* to = auth/confirm-email */)
			}
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewVerifiedEmailPage() {
	VerifiedEmailPage()
}
