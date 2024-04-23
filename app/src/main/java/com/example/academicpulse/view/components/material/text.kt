package com.example.academicpulse.view.components.material

import androidx.compose.material3.Text as UIText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.academicpulse.R
import com.example.academicpulse.utils.Res

private val font = FontFamily(Font(R.font.regular_font))

@Composable
fun Text(text: String = "") {
	UIText(text = text, fontFamily = font)
}

@Composable
fun Text(text: Int) {
	Text(Res.string(text))
}

@Composable
fun Title(text: String) {
	UIText(text = text, fontWeight = FontWeight.Bold, fontFamily = font)
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
		fontSize = 14.sp,
		lineHeight = 17.sp,
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
		fontSize = 27.sp,
		textAlign = align,
	)
}
