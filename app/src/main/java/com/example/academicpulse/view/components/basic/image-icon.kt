package com.example.academicpulse.view.components.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.academicpulse.utils.Res

@Composable
fun Image(
	modifier: Modifier = Modifier,
	id: Int,
	align: Alignment = Alignment.Center,
	scale: ContentScale = ContentScale.Fit,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null
) {
	androidx.compose.foundation.Image(
		modifier = modifier,
		imageVector = Res.drawable(id),
		contentDescription = null,
		alignment = align,
		contentScale = scale,
		alpha = alpha,
		colorFilter = colorFilter,
	)
}

@Composable
fun Icon(
	modifier: Modifier = Modifier,
	id: Int,
	size: Dp = 14.dp,
	color: Color = LocalContentColor.current,
	onClick: (() -> Unit)? = null
) {
	var internalModifier = modifier.size(size)
	if (onClick != null) internalModifier = internalModifier.clickable { onClick() }

	androidx.compose.material3.Icon(
		modifier = internalModifier,
		imageVector = Res.drawable(id),
		contentDescription = null,
		tint = color,
	)
}
