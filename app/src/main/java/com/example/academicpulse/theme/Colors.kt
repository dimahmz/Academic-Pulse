package com.example.academicpulse.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val primary = Color(0xFF0B8AA0)
val secondary = Color(0xFFEAF2FF)
val error = Color(0xFFD70000)
val white = Color(0xFFFFFFFF)
val navBavIconColor = Color(0xFF74757D)
val orange = Color(0xFFFFC107)
val red = Color(0xFFF44336)
val borderColor = Color(0xFFC5C6CC)
val textColor = Color(0xFF0B0B0B)
val titleColor = Color(0xFF0B0B0B)
val descriptionColor = Color(0xFF71727A)

val inputGray = Color(0xFFCCCCCC)

val DarkColorScheme = darkColorScheme(
	primary = primary,
	secondary = secondary,
	tertiary = white,
	background = white,
	error = error,
	outline = borderColor,
	surface = white,
)

val LightColorScheme = lightColorScheme(
	primary = primary,
	secondary = secondary,
	tertiary = white,
	background = white,
	error = error,
	outline = borderColor,
	surface = white,
)
