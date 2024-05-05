package com.example.academicpulse.view.components.global

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.h1TextSize
import com.example.academicpulse.view.components.basic.H1
import com.example.academicpulse.view.components.basic.Icon

@Composable
fun Header(title: Int, arrow: Boolean = true) {
	Box(
		modifier = Modifier
			.padding(vertical = 25.dp)
			.fillMaxWidth(),
		contentAlignment = Alignment.CenterStart
	) {
		if (arrow)
			Icon(
				id = R.drawable.icon_left_arrow,
				color = MaterialTheme.colorScheme.primary,
				size = (h1TextSize.value * 20 / 23).dp,
				onClick = { Router.back() }
			)

		H1(
			text = title,
			modifier = Modifier.fillMaxWidth(),
			align = TextAlign.Center
		)
	}
}
