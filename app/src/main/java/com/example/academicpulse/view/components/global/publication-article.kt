package com.example.academicpulse.view.components.global

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.User
import com.example.academicpulse.theme.descriptionTextSize
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicationArticle(publication: Publication) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = pagePaddingX, horizontal = (pagePaddingX.value / 2).dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Column {
			H3(text = publication.title) {
				logcat("User click: Show publication by id {${publication.id}}")
			}
			Text(text = publication.date)
		}

		// Note: Use FlowRow instead of Row for the (overflow: break) option
		FlowRow(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(gap),
		) {
			publication.contributors.forEach { contributor ->
				Row(
					horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.clickable {
						if (contributor.id == Store.auth.auth.currentUser?.uid)
							logcat("User click: Show current user profile")
						else
							logcat("User click: Show another user profile by id {${contributor.id}}")
					}
				) {
					Image(image = contributor.profile, size = 18.dp)
					Text(text = contributor.name)
				}
			}
		}

		Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
			Row(horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp)) {
				Title(text = publication.reads.toString(), size = descriptionTextSize)
				Text(text = R.string.reads, size = descriptionTextSize)
			}
			Row(horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp)) {
				Title(text = publication.uploads.toString(), size = descriptionTextSize)
				Text(text = R.string.uploads, size = descriptionTextSize)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewPublicationArticle() {
	PublicationArticle(
		Publication(
			id = "92163248id",
			title = "Load Balancing in Cloud Environment: A State-of-the-Art Review",
			date = "Jun 2024",
			reads = 60,
			uploads = 60,
			contributors = arrayListOf(
				User(id = "9873210", name = "John Deo", image = null),
				User(id = "0123246", name = "John Deo", image = null),
				User(id = "1324923", name = "John Deo", image = null),
			)
		)
	)
}
