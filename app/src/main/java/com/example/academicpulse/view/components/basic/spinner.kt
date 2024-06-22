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
	inline: Boolean = false,
	color: Color? = null,
	size: Dp = if (inline) 20.dp else 30.dp,
	stroke: Dp = (size.value * 1.7 / 20).dp,
) {
	if (inline)
		CircularProgressIndicator(
			modifier = modifier.size(size),
			color = color ?: MaterialTheme.colorScheme.primary,
			strokeWidth = stroke,
		)
	else
		Box(
			modifier = modifier
				.fillMaxWidth()
				.padding(top = 30.dp),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator(
				modifier = Modifier.size(size),
				color = color ?: MaterialTheme.colorScheme.primary,
				strokeWidth = stroke,
			)
		}
}
