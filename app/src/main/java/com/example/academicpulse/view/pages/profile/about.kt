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
import androidx.compose.ui.tooling.preview.Preview
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
		Header(title = R.string.sign_up)
		Spacer(Modifier.height(14.dp))

		Column(verticalArrangement = Arrangement.spacedBy((gap.value * 1.7).dp)) {
			Text(
				text = "AcademicPulse is for you: the scientist, the clinician, the student, the  engineer, the public health worker, the lab technician, the computer  scientist.",
				size = 13.sp,
			)
			Text(
				text = "The work you do is important. But too often, you are  overworked, underpaid, have low job security, and operate under high  pressure. Becoming a researcher in the first place was no easy task, and  access all too often relies on privilege. Our purpose is to make your  life easier.",
				size = 13.sp,
			)
			Text(
				text = "We offer a home for you—a place to share your work  and connect with peers around the globe, traversing the borders and  silos of science. We want to empower you to do your best work, advance  your field, and make a better world for all.",
				size = 13.sp,
			)
			Text(
				text = "For the  intellectually curious, the seekers of truth, the geeks, the makers: our  members offer you a treasure trove of knowledge in areas as diverse as  materials science, agriculture, human health, and quantum physics.",
				size = 13.sp,
			)
		}
	}

	BackHandler { Router.back() }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAboutPage() {
	AboutPage()
}
