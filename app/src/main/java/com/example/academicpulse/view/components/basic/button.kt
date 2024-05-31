package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.academicpulse.theme.inputHeight
import com.example.academicpulse.theme.radius

@Composable
fun Button(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	ghost: Boolean = false,
	icon: Int? = null,
	loading: Boolean = false,
	disabled: Boolean = loading,
	height: Dp = inputHeight,
	onClick: (() -> Unit)? = null
) {
	Button(
		modifier = modifier,
		text = stringResource(text),
		ghost = ghost,
		icon = icon,
		loading = loading,
		disabled = disabled,
		height = height,
		onClick = onClick,
	)
}

@Composable
fun Button(
	modifier: Modifier = Modifier,
	text: String? = null,
	ghost: Boolean = false,
	icon: Int? = null,
	loading: Boolean = false,
	disabled: Boolean = loading,
	height: Dp = inputHeight,
	onClick: (() -> Unit)? = null
) {
	val colors = MaterialTheme.colorScheme

	androidx.compose.material3.Button(
		onClick = { if (!disabled) onClick?.invoke() },
		shape = RoundedCornerShape(radius),
		modifier = modifier
			.border(
				width = 1.dp,
				color = if (disabled) colors.primary.copy(alpha = 0.4F) else colors.primary,
				shape = RoundedCornerShape(radius),
			)
			.fillMaxWidth()
			.height(height),
		enabled = !disabled,
		colors = ButtonDefaults.buttonColors(
			containerColor = if (ghost) Color.White else colors.primary,
			contentColor = if (!ghost) Color.White else colors.primary,
			disabledContainerColor = colors.secondary.copy(alpha = 0.7F),
			disabledContentColor = Color(0xFFCCCCCC),
		),
	) {
		if (icon != null && !loading) Icon(id = icon, color = LocalContentColor.current)
		else if (loading) Spinner(color = LocalContentColor.current)
		if ((icon != null || loading) && text != null) Spacer(Modifier.width(8.dp))
		if (text != null) Text(text = text, color = LocalContentColor.current)
	}
}
