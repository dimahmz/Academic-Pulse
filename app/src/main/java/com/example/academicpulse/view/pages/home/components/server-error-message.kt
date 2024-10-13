package com.example.academicpulse.view.pages.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.view.components.basic.Button
import com.example.academicpulse.view.components.basic.Description
import com.example.academicpulse.view.components.basic.H2
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource


@Composable
fun HomeServerErrorMessage(onTryAgain: () -> Unit) {
	Column(
		modifier = Modifier.fillMaxSize(), // Fill the entire screen
		verticalArrangement = Arrangement.Center, // Center vertically
		horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
	) {
		Icon(
			imageVector = Icons.Default.Info, // Use a default error icon or custom icon
			contentDescription = stringResource(id = R.string.server_error_icon_desc), // Accessibility text
			modifier = Modifier
				.size(64.dp)
				.padding(bottom = 16.dp), // Padding between the icon and the title
			tint = Color.Red // Icon color
		)
		H2(text = R.string.server_error_title)
		Description(
			text = R.string.server_error_description, modifier = Modifier.padding(horizontal = 36.dp)
		)
		Spacer(Modifier.size(20.dp))
		Button(
			text = R.string.try_again,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
		) {
			onTryAgain()
		}
	}
}
