package com.example.academicpulse.view.pages.notifications

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.example.academicpulse.router.Router
import com.example.academicpulse.view.components.basic.Text

@Composable
fun NotificationPage() {
	Text(text = "Notification page")

	BackHandler { Router.navigate("home") }
}
