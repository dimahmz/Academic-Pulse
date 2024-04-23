package com.example.academicpulse.view.components.material

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text as UIText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.academicpulse.R
import com.example.academicpulse.theme.fontsColorText
import com.example.academicpulse.theme.h1TextSize
import com.example.academicpulse.theme.textSize
import com.example.academicpulse.utils.Res

private val font = FontFamily(Font(R.font.regular_font))

@Composable
fun Text(text: String = "", color: Color = LocalContentColor.current) {
	UIText(text = text, fontFamily = font, color = color, fontSize = textSize,)
}

@Composable
fun Text(text: Int) {
	Text(Res.string(text))
}

@Composable
fun Title(text: String) {
	UIText(
		text = text,
		fontWeight = FontWeight.Bold,
		fontFamily = font,
		fontSize = textSize,
	)
}

@Composable
fun Title(text: Int) {
	Title(Res.string(text))
}

@Composable
fun Description(text: String) {
	UIText(
		text = text,
		fontWeight = FontWeight.Light,
		fontFamily = font,
		fontSize = textSize,
		lineHeight = (textSize.value * 10 / 7).sp,
		color = fontsColorText
	)
}

@Composable
fun Description(text: Int) {
	Description(Res.string(text))
}

@Composable
fun H1(
	modifier : Modifier = Modifier,
	text: String,
	align: TextAlign = TextAlign.Start
) {
	UIText(
		modifier = modifier,
		text = text,
		fontWeight = FontWeight.Bold,
		fontFamily = font,
		fontSize = h1TextSize,
		textAlign = align,
	)
}
