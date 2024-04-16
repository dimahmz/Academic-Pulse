package com.example.academicpulse.view.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.academicpulse.router.Router

@Composable
fun Page(sharedViewModel: ViewModel, router: Router, root: String, route: String, navigateTo: String) {
	Text("Current: $root - $route")
	Button(onClick = {
		router.redirect(navigateTo, root)
	}) {
		Text("Navigate to: $root - $navigateTo")
	}
}
