package com.example.academicpulse.view.components.basic

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.academicpulse.theme.textColor

@Composable
fun Image(
	modifier: Modifier = Modifier,
	image: ImageVector,
	size: Dp? = null,
	height: Dp? = size,
	width: Dp? = height,
	align: Alignment = Alignment.Center,
) {
	var modified = modifier
	if (height != null) modified = modified.height(height)
	if (width != null) modified = modified.height(width)

	androidx.compose.foundation.Image(
		modifier = modified,
		imageVector = image,
		contentDescription = null,
		alignment = align,
	)
}

@Composable
fun Image(
	modifier: Modifier = Modifier,
	@DrawableRes id: Int,
	size: Dp? = null,
	height: Dp? = size,
	width: Dp? = height,
	align: Alignment = Alignment.Center,
) {
	Image(
		modifier = modifier,
		image = ImageVector.vectorResource(id),
		size = size,
		height = height,
		width = width,
		align = align,
	)
}

@Composable
fun Icon(
	modifier: Modifier = Modifier,
	@DrawableRes id: Int,
	color: Color = textColor,
	size: Dp = 14.dp,
	height: Dp? = size,
	width: Dp? = height,
	onClick: (() -> Unit)? = null
) {
	var modified = modifier
	if (height != null) modified = modified.height(height)
	if (width != null) modified = modified.height(width)
	if (onClick != null) modified = modified.clickable { onClick() }

	androidx.compose.material3.Icon(
		modifier = modified,
		imageVector = ImageVector.vectorResource(id),
		contentDescription = null,
		tint = color,
	)
}
