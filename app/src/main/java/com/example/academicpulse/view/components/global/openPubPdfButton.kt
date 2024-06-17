package com.example.academicpulse.view.components.global

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import com.example.academicpulse.view.components.basic.Button

@Composable
fun openPublicationPdfButton() {

	Button(
		text = R.string.full_text,
		icon = R.drawable.icon_open_link,
		modifier = Modifier.wrapContentWidth()
	)
}

@Preview(showBackground = true)
@Composable
fun viewOpenPublicationPdfButton() {
	openPublicationPdfButton()
}
