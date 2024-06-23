package com.example.academicpulse.view.components.global

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.orange
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.theme.red
import com.example.academicpulse.theme.white
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@Composable
fun PublicationArticle(publication: Publication, isProfilePage: Boolean = false) {
	Column(
		verticalArrangement = Arrangement.spacedBy(4.dp),
		modifier = Modifier
			.fillMaxWidth()
			.clickable {
				Store.publications.selectedPublicationId = publication.id
				Store.publications.redirectedFromProfile = isProfilePage
				Router.navigate("publications/one-publication")
			}
			.padding(vertical = pagePaddingX, horizontal = (pagePaddingX.value / 2).dp),
	) {
		H2(text = publication.title, maxLines = 3)
		Description(text = publication.abstract, maxLines = 2)
		if (publication.authors.isNotEmpty())
			AuthorsRow(authors = publication.authors, showProfileOnClick = true, isProfilePage = isProfilePage)
		Spacer(Modifier.width(4.dp))
		Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
			Description(text = Publication.formatDate(publication.date))
			Spacer(Modifier.weight(1f))
			if (publication.status != "accepted") {
				val color = if (publication.status == "pending") orange else red
				val statusText =
					if (publication.status == "pending") R.string.pending_status else R.string.rejected_status
				Ticket(text = stringResource(statusText), color)
				Spacer(Modifier.width(4.dp))
			}
			Ticket(text = publication.type, color = MaterialTheme.colorScheme.primary)
		}
	}
}

@Composable
fun Ticket(text: String, color: Color) {
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(6.dp))
			.background(color)
			.padding(horizontal = 10.dp, vertical = 4.dp), contentAlignment = Alignment.Center
	) {
		Text(text = text, color = white, size = 11.sp)
	}
}
