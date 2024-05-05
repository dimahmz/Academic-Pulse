package com.example.academicpulse.view.components.basic

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Spinner(
	modifier: Modifier = Modifier,
	color: Color? = null,
	size: Dp = 20.dp,
	stroke: Dp = (size.value * 1.7 / 20).dp,
) {
	CircularProgressIndicator(
		modifier = modifier.size(size),
		color = color ?: MaterialTheme.colorScheme.primary,
		strokeWidth = stroke,
	)
}
