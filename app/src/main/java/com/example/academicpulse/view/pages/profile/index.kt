package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.copy
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.UserCard
import com.example.academicpulse.view.components.global.PublicationArticle

val publicationsLiveData = MutableLiveData(arrayListOf<Publication>())

@Composable
fun ProfilePage() {
	val (publicationsCount, setPublicationsCount) = useState(0)
	val publications = useAtom(publicationsLiveData)

	fun addPublication() {
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
		val list = publicationsLiveData.value?.copy()
		list?.add(publication)
		publicationsLiveData.value = list
	}

	LazyColumn(modifier = Modifier.padding(bottom = bottomBarHeight)) {
		item {
			UserCard(onAddResearch = ::addPublication)
			Line(height = 5.dp)
		}
		if (publications != null)
			items(publications) {
				Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp)) {
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
