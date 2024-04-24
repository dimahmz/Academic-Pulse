package com.example.academicpulse.view.components.material

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.academicpulse.theme.textSize
import com.example.academicpulse.utils.Res

private val font = FontFamily(Font(R.font.regular_font))

@Composable
fun Text(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = LocalContentColor.current,
	size: TextUnit = textSize,
	weight: FontWeight? = null,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
) {
	androidx.compose.material3.Text(
		modifier = modifier,
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
	text: Int,
	color: Color = LocalContentColor.current,
	size: TextUnit = textSize,
	weight: FontWeight? = null,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
) {
	Text(
		modifier = modifier,
		text = Res.string(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		weight = weight,
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
) {
	Text(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		weight = FontWeight.Bold,
	)
}

@Composable
fun Title(
	modifier: Modifier = Modifier,
	text: Int,
	color: Color = titleColor,
	size: TextUnit = textSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
) {
	Title(
		modifier = modifier,
		text = Res.string(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
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
) {
	Text(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
		weight = FontWeight.Light,
	)
}

@Composable
fun Description(
	modifier: Modifier = Modifier,
	text: Int,
	color: Color = descriptionColor,
	size: TextUnit = descriptionTextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
) {
	Description(
		modifier = modifier,
		text = Res.string(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
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
) {
	Title(
		modifier = modifier,
		text = text,
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
	)
}

@Composable
fun H1(
	modifier: Modifier = Modifier,
	text: Int,
	color: Color = titleColor,
	size: TextUnit = h1TextSize,
	align: TextAlign = TextAlign.Start,
	maxLines: Int = Int.MAX_VALUE,
) {
	H1(
		modifier = modifier,
		text = Res.string(text),
		color = color,
		size = size,
		align = align,
		maxLines = maxLines,
	)
}
