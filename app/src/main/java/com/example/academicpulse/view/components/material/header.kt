package com.example.academicpulse.view.components.material

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.Res

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
				size = 20.dp,
			) {
				Router.back()
			}
		H1(
			text = Res.string(title),
			modifier = Modifier.fillMaxWidth(),
			align = TextAlign.Center
		)
	}
}
