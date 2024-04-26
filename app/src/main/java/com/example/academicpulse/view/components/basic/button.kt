package com.example.academicpulse.view.components.basic

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.academicpulse.theme.inputHeight
import com.example.academicpulse.theme.radius
import com.example.academicpulse.utils.Res

@Composable
fun Button(
	modifier: Modifier = Modifier,
	text: String? = null,
	ghost: Boolean = false,
	icon: Int? = null,
	onClick: (() -> Unit)? = null
) {
	Button(
		onClick = { onClick?.invoke() },
		shape = RoundedCornerShape(radius),
		modifier = modifier
			.fillMaxWidth()
			.border(
				width = 1.dp,
				color = MaterialTheme.colorScheme.primary,
				shape = RoundedCornerShape(radius)
			)
			.height(inputHeight),
		colors = ButtonDefaults.buttonColors(
			containerColor = if (ghost) Color.White else MaterialTheme.colorScheme.primary,
			contentColor = if (!ghost) Color.White else MaterialTheme.colorScheme.primary,
		),
	) {
		if (icon != null) {
			Icon(id = icon)
			if (text != null) Spacer(Modifier.width(8.dp))
		}
		if (text != null) Text(text = text, color = LocalContentColor.current)
	}
}

@Composable
fun Button(
	modifier: Modifier = Modifier,
	text: Int,
	ghost: Boolean = false,
	icon: Int? = null,
	onClick: (() -> Unit)? = null
) {
	Button(
		modifier = modifier,
		text = Res.string(text),
		ghost = ghost,
		icon = icon,
		onClick = onClick,
	)
}
