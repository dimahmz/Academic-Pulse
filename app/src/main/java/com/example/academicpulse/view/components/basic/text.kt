package com.example.academicpulse.view.components.basic

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.academicpulse.R
import com.example.academicpulse.theme.titleColor
import com.example.academicpulse.theme.descriptionColor
import com.example.academicpulse.theme.descriptionTextSize
import com.example.academicpulse.theme.h1TextSize
import com.example.academicpulse.theme.h2TextSize
import com.example.academicpulse.theme.h3TextSize
import com.example.academicpulse.theme.textColor
import com.example.academicpulse.theme.textSize

private val font = FontFamily(Font(R.font.regular_font))

@Composable
fun Text(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = textColor,
	size: TextUnit = textSize,
	weight: FontWeight? = null,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	var modified = modifier
	if (onClick != null) modified = modified.clickable(onClick = onClick)

	androidx.compose.material3.Text(
		modifier = modified,
		text = text,
		fontFamily = font,
		color = color,
		fontSize = size,
		textAlign = align,
		maxLines = maxLines,
		lineHeight = (size.value * 10 / 7).sp,
		fontWeight = weight,
	)
}

@Composable
fun Text(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	color: Color = textColor,
	size: TextUnit = textSize,
	weight: FontWeight? = null,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Text(
		modifier = modifier,
		text = stringResource(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		weight = weight,
		onClick = onClick,
	)
}

@Composable
fun Title(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = titleColor,
	size: TextUnit = textSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Text(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		weight = FontWeight.Bold,
		onClick = onClick,
	)
}

@Composable
fun Title(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	color: Color = titleColor,
	size: TextUnit = textSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Title(
		modifier = modifier,
		text = stringResource(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun Description(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = descriptionColor,
	size: TextUnit = descriptionTextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Text(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		weight = FontWeight.Light,
		onClick = onClick,
	)
}

@Composable
fun Description(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	color: Color = descriptionColor,
	size: TextUnit = descriptionTextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Description(
		modifier = modifier,
		text = stringResource(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun H1(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = titleColor,
	size: TextUnit = h1TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Title(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun H1(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	color: Color = titleColor,
	size: TextUnit = h1TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	H1(
		modifier = modifier,
		text = stringResource(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun H2(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = titleColor,
	size: TextUnit = h2TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Title(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun H2(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	color: Color = titleColor,
	size: TextUnit = h2TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	H2(
		modifier = modifier,
		text = stringResource(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun H3(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = titleColor,
	size: TextUnit = h3TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	Title(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}

@Composable
fun H3(
	modifier: Modifier = Modifier,
	@StringRes text: Int,
	color: Color = titleColor,
	size: TextUnit = h3TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
	onClick: (() -> Unit)? = null,
) {
	H3(
		modifier = modifier,
		text = stringResource(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		onClick = onClick,
	)
}
