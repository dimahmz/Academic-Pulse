package com.example.academicpulse.view.components.global

import androidx.compose.foundation.layout.Row
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
fun Header(title: Int, navBarVisibleAfterBack: Boolean = false) {
	Header(title) { Router.back(navBarVisibleAfterBack) }
}

@Composable
fun Header(title: Int, suffix: @Composable (() -> Unit)? = null, onClick: () -> Unit) {
	Row(
		modifier = Modifier
			.padding(vertical = 25.dp)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			id = R.drawable.icon_left_arrow,
			color = MaterialTheme.colorScheme.primary,
			size = (h1TextSize.value * 20 / 23).dp,
			onClick = onClick
		)

		H1(
			text = title,
			modifier = Modifier.weight(1f),
			align = TextAlign.Center
		)

		suffix?.invoke()
	}
}
