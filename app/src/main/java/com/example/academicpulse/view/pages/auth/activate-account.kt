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
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.*

@Composable
fun ActivateAccountPage() {
	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
			Image(id = R.drawable.icon_receive_email, modifier = Modifier.size(70.dp))
		}

		Spacer(Modifier.height(20.dp))
		H1(text = R.string.inactive_account, align = TextAlign.Center)
		Description(text = R.string.inactive_account_description, align = TextAlign.Center)

		Button(
			text = R.string.back_login,
			modifier = Modifier.padding(vertical = 30.dp)
		) {
			Router.navigate("auth/login-user", false)
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewActivateAccountPage() {
	ActivateAccountPage()
}
