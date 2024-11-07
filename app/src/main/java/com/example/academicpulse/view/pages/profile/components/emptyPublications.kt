package com.example.academicpulse.view.pages.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.view.components.basic.Description

@Preview(showBackground = true)
@Composable
fun EmptyPublicationsMessage() {
	Column(
		modifier = Modifier
			.fillMaxSize(), // Fill the entire screen
		verticalArrangement = Arrangement.Center, // Center vertically
		horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
	){
		Description(text = R.string.no_publication )
	}
}