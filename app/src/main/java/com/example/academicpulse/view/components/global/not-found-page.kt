package com.example.academicpulse.view.components.global

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.academicpulse.view.components.basic.Button
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.example.academicpulse.R
import com.example.academicpulse.view.components.basic.Text

@Composable
fun NotFoundPage(message: Int, onRetry: () -> Unit) {
	Column(
		modifier = Modifier.fillMaxSize().padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = message
		)
		Spacer(modifier = Modifier.height(16.dp))
		Button(text = R.string.try_again) {
			onRetry()
		}
	}
}
