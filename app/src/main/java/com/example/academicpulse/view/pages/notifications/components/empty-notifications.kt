package com.example.academicpulse.view.pages.notifications.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.view.components.basic.Description
import com.example.academicpulse.view.components.basic.H2

@Preview(showBackground = true)
@Composable
fun EmptyNotificationMessage() {
	Column(
		modifier = Modifier
			.fillMaxSize(), // Fill the entire screen
		verticalArrangement = Arrangement.Center, // Center vertically
		horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
	){
	H2(text = R.string.no_notifications )
	Description(text =R.string.no_notifications_description ,  modifier = Modifier.padding(horizontal = 36.dp) )
	}
}
