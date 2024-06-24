package com.example.academicpulse.view.pages.publications

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.H2
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store
import com.example.academicpulse.model.Publication
import com.example.academicpulse.theme.descriptionTextSize
import com.example.academicpulse.view.components.basic.Title
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.utils.context
import com.example.academicpulse.view.components.basic.Button
import com.example.academicpulse.view.components.global.AuthorsRow
import com.example.academicpulse.view.components.global.PublicationSettings

@Composable
fun OnePublicationPage() {
	val (loading, setLoading) = useState { true }
	val (publication, setPublication) = useState<Publication?> { null }

	LaunchedEffect(Unit) {
		Store.publications.fetchSelected(
			onError = { setLoading(false) },
			onSuccess = {
				setPublication(it)
				setLoading(false)
			},
		)
	}

	val (back, setBack) = useState { {} }
	LaunchedEffect(loading) {
		if (loading) setBack {}
		else setBack {
			val fromForm = Store.publications.redirectedFromForm
			val fromProfile = Store.publications.redirectedFromProfile
			Store.publications.redirectedFromForm = false
			Store.publications.redirectedFromProfile = false
			Router.back(
				target = if (fromForm || fromProfile) "profile" else "home",
				step = if (fromForm) 2 else 1,
			)
		}
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.publication, onClick = back, suffix = {
			if (Store.publications.belongsToThisUser(publication)) {
				PublicationSettings(onLoadingChange = setLoading, onDelete = back)
			}
		})
		Spacer(Modifier.height(14.dp))

		if (loading) Spinner()
		else if (publication != null) {
			Spacer(Modifier.height(14.dp))
			Box(
				modifier = Modifier
					.wrapContentSize()
					.clip(RoundedCornerShape(6.dp))
					.background(MaterialTheme.colorScheme.primary)
					.padding(horizontal = 30.dp, vertical = 8.dp), contentAlignment = Alignment.Center
			) {
				Text(text = publication.type, color = MaterialTheme.colorScheme.background)
			}
			Spacer(Modifier.height(15.dp))
			H2(text = publication.title)

			// Publication's Date
			Spacer(Modifier.height(10.dp))
			Text(text = Publication.formatDate(publication.date))

			// Publication's File
			publication.fetchFile()
			if (!publication.fileAvailability) {
				Row {
					Spacer(Modifier.weight(1f))
					Spinner(inline = true)
					Spacer(Modifier.width(4.dp))
					Text(text = R.string.file_is_loading)
					Spacer(Modifier.weight(1f))
				}
			} else if (publication.file != null) {
				Spacer(Modifier.height(10.dp))
				Button(
					text = R.string.view_file,
					icon = R.drawable.icon_open_link,
					modifier = Modifier.wrapContentWidth()
				) { Router.navigate("publications/pdf-viewer") }
			}

			// Publication's DOI
			if (publication.doi.isNotBlank()) {
				Spacer(Modifier.height(15.dp))
				Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
					Title(text = "DOI")
					Text(text = publication.doi, size = descriptionTextSize, underlined = true) {
						val url = "https://www.doi.org/${publication.doi}"
						val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
						context.startActivity(intent)
					}
				}
			}
			Spacer(Modifier.height(10.dp))

			// Publication's Authors
			AuthorsRow(authors = publication.authors, showProfileOnClick = true)
			Spacer(Modifier.height(15.dp))
			Line(height = 2.dp)
			Spacer(Modifier.height(15.dp))

			// Publication's Abstract
			H2(text = "Abstract")
			Spacer(Modifier.height(10.dp))
			Text(text = publication.abstract, size = descriptionTextSize)
			Spacer(Modifier.height(5.dp))
		}
	}

	BackHandler(onBack = back)
}
