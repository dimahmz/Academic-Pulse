package com.example.academicpulse.view.components.global

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store
import com.google.firebase.Timestamp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicationArticle(publication: Publication) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = pagePaddingX, horizontal = (pagePaddingX.value / 2).dp)
			.clickable {
				Store.publications.selectedPublicationId = publication.id
				Router.navigate("publications/one-publication", false)
			},
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Column {
			H2(text = publication.title, modifier = Modifier.fillMaxWidth())
			Spacer(Modifier.height(10.dp))
			Box(
				modifier = Modifier
					.wrapContentSize()
					.clip(RoundedCornerShape(12.dp))
					.background(MaterialTheme.colorScheme.primary)
					.padding(horizontal = 20.dp, vertical = 8.dp), contentAlignment = Alignment.Center
			) {
				Text(text = publication.type, color = MaterialTheme.colorScheme.background)
			}
			Spacer(Modifier.height(10.dp))
			Text(text = Publication.formatDate(publication.date))
		}
		// Note: Use FlowRow instead of Row for the (overflow: break) option
		/*FlowRow(
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
					// Text(text = contributor.name)
				}
			}
		}
		*/

		/*	Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
				Row(horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp)) {
					Title(text = "${publication.reads}", size = descriptionTextSize)
					Text(text = R.string.reads, size = descriptionTextSize)
				}
				Row(horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp)) {
					Title(text = publication.uploads.toString(), size = descriptionTextSize)
					Text(text = R.string.uploads, size = descriptionTextSize)
				}
			}*/
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewPublicationArticle() {
	PublicationArticle(
		Publication(
			id = "92163248id",
			typeId = "id",
			title = "Load Balancing in Cloud Environment: A State-of-the-Art Review",
			abstract = "Example of abstract",
			doi = "Example of doi",
			date = Timestamp(1800000000, 0),
			reads = 60,
			uploads = 60,
		)
	)
}
