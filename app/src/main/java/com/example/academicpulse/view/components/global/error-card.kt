package com.example.academicpulse.view.components.global

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.h1TextSize
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Text

@Composable
fun ErrorMessage(@StringRes errorMessage: Int = R.string.unknown_error) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(16.dp)
			.border(
				width = 1.dp, color = MaterialTheme.colorScheme.error, shape = RoundedCornerShape(8.dp)
			)
			.background(Color.Red.copy(alpha = 0.1f))
			.padding(16.dp), contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				id = R.drawable.icon_about,
				color = MaterialTheme.colorScheme.error,
				size = (h1TextSize.value * 20 / 23).dp,
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = errorMessage,
				color = MaterialTheme.colorScheme.error,
				weight = FontWeight.Bold,
				align = TextAlign.Center
			)
		}
	}
}
