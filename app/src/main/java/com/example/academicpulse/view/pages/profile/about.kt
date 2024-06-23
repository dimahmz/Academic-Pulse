package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.Header

@Composable
fun AboutPage() {
	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.about) { Router.back("profile/settings") }
		Spacer(Modifier.height(14.dp))

		Column(verticalArrangement = Arrangement.spacedBy((gap.value * 1.7).dp)) {
			Text(text = R.string.about_description_1, size = 13.sp)
			Text(text = R.string.about_description_2, size = 13.sp)
			Text(text = R.string.about_description_3, size = 13.sp)
			Text(text = R.string.about_description_4, size = 13.sp)
		}
	}

	BackHandler { Router.back("profile/settings") }
}
