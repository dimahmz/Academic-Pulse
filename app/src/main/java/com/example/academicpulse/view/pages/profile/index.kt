package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.copy
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.ProfileCard
import com.example.academicpulse.view.components.global.PublicationArticle

@Composable
fun ProfilePage() {
	val (publicationsCount, setPublicationsCount) = useState(0)
	val (publications, setPublications) = useState({ arrayListOf<Publication>() })
	Column(modifier = Modifier.fillMaxHeight()) {
		ProfileCard {
			logcat("User click: Add research")
			val publication = Publication(
				id = "92163248id${publicationsCount}",
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
			setPublicationsCount(publicationsCount + 1)
			val list = publications.copy()
			list.add(publication)
			setPublications(list)
		}
		Line(height = 5.dp)

		// TODO: Use a LazyColumn
		publications.forEach {
			Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp) ) {
				PublicationArticle(it)
				Line(height = 1.dp)
			}
		}
	}

	BackHandler { Router.replace("home", true) }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewProfilePage() {
	ProfilePage()
}
