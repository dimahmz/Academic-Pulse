package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view_model.Store

@Composable
fun SettingsPage() {
	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.sign_up)
		Spacer(Modifier.height(14.dp))

		Element("Edit profile", R.drawable.icon_edit) {
			// Edit Profile
		}
		Element("About", R.drawable.icon_about) {
			// navigate to About
		}
		Element("Log out", R.drawable.icon_logout) {
			Store.auth.logOut()
		}
	}

	BackHandler { Router.back() }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSettingsPage() {
	SettingsPage()
}

@Composable
fun Element(text: String, @DrawableRes icon: Int, onClick: () -> Unit) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 6.dp)
			.clickable(onClick = onClick)
			.padding(vertical = 14.dp, horizontal = 10.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(text = text)
		Icon(id = icon, size = 18.dp)
	}
	Line(height = 2.dp)
}
