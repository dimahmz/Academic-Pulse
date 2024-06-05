package com.example.academicpulse.view.pages.publication

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useAtom
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

@Composable
fun OnePublicationPage() {
	val (loading, setLoading) = useState { true }
	val publication = useAtom(Store.publications.publication)
	LaunchedEffect(true) {
		Store.publications.fetchSelected(onSuccess = { setLoading(false) }) {
			setLoading(false)
		}
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(
			title = R.string.publication,
		) {
			val redirectedFromForm = Store.publications.redirectedFromForm
			Store.publications.redirectedFromForm = false
			Router.back(
				navBarVisible = true, step = if (redirectedFromForm) 2 else 1
			)
		}

		PublicationSettings()
		Spacer(Modifier.height(14.dp))
		if (loading) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 30.dp), contentAlignment = Alignment.Center
			) {
				Spinner(size = 30.dp)
			}
		} else if (publication != null) {
			Spacer(Modifier.height(14.dp))
			H2(text = publication.title)
			Spacer(Modifier.height(14.dp))
			Text(text = Publication.formatDate(publication.date))
			Spacer(Modifier.height(10.dp))
			Box(
				modifier = Modifier
					.wrapContentSize()
					.clip(RoundedCornerShape(6.dp))
					.background(MaterialTheme.colorScheme.primary)
					.padding(horizontal = 30.dp, vertical = 8.dp), contentAlignment = Alignment.Center
			) {
				Text(text = "Type", color = MaterialTheme.colorScheme.background)
			}
			Spacer(Modifier.height(10.dp))
			Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
				Title(text = "DOI")
				Text(text = publication.doi, size = descriptionTextSize, underlined = true) {
					val url = "https://www.doi.org/${publication.doi}"
					val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
					context.startActivity(intent)
				}
			}
			Spacer(Modifier.height(15.dp))
			Line(height = 2.dp)
			Spacer(Modifier.height(15.dp))
			H2(text = "Abstract")
			Spacer(Modifier.height(10.dp))
			Text(text = publication.abstract, size = descriptionTextSize)
			Spacer(Modifier.height(5.dp))
		}
	}

	BackHandler {
		val redirectedFromForm = Store.publications.redirectedFromForm
		Store.publications.redirectedFromForm = false    /* to = profile/index or home/index */
		Router.back(
			navBarVisible = true, step = if (redirectedFromForm) 2 else 1
		)
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewOnePublicationPage() {
	OnePublicationPage()
}
