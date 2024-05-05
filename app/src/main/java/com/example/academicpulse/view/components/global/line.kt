package com.example.academicpulse.view.components.global

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun Line(height: Dp, color: Color = MaterialTheme.colorScheme.outline) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(color)
	) {
		Spacer(Modifier.height(height))
	}
}
