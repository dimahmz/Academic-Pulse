package com.example.academicpulse.view.components.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.academicpulse.utils.Res

@Composable
fun Icon(
	id: Int,
	size: Dp = 14.dp,
	color: Color = LocalContentColor.current,
	onClick: (() -> Unit)? = null
) {
	val modifier = (if (onClick != null) Modifier.clickable { onClick() } else Modifier).size(size)

	Icon(
		modifier = modifier,
		imageVector = Res.drawable(id),
		contentDescription = null,
		tint = color,
	)
}