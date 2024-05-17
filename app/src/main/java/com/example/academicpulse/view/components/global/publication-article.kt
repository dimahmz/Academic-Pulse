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
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.PublicationType
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.descriptionTextSize
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

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
			H3(text = publication.title, modifier = Modifier.fillMaxWidth())
			val date = Date(publication.date.seconds * 1000L + publication.date.nanoseconds / 1000000L)
			val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
			val formattedDate = sdf.format(date)
			Text(text = formattedDate)
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
			typeId = PublicationType.list[0].id,
			title = "Load Balancing in Cloud Environment: A State-of-the-Art Review",
			abstract = "Example of abstract",
			doi = "Example of doi",
			date = Timestamp(1800000000, 0),
			reads = 60,
			uploads = 60,
		)
	)
}
