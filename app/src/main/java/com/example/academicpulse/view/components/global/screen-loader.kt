package com.example.academicpulse.view.components.global

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.view.components.basic.Spinner

@Composable
fun LoaderScreen() {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.blur(radius = 3.dp)
			.background(color = Color.Black.copy(alpha = 0.5f))
			.blur(radius = 3.dp),
		contentAlignment = Alignment.Center
	) {
		Spinner(size = 120.dp)
	}
}

@Preview(showBackground = true)
@Composable
fun LoaderScreenPreview() {
	LoaderScreen()
}
