package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.ProfileCard

@Composable
fun ProfilePage() {
	Column(modifier = Modifier.fillMaxHeight()) {
		ProfileCard(
			onAddResearch = {
				logcat("User click: Add research")
			}
		)
		Line(height = 5.dp)
	}

	BackHandler { Router.replace("home", true) }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfilePage() {
	ProfilePage()
}
