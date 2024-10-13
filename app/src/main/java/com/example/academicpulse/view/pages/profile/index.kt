package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.ComponentServerErrorMessage
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.UserCard
import com.example.academicpulse.view.components.global.PublicationArticle
import com.example.academicpulse.view.pages.profile.components.EmptyPublicationsMessage
import com.example.academicpulse.view.pages.profile.components.UserPublicationsTab
import com.example.academicpulse.view_model.Store

@Composable
fun ProfilePage() {
	val (loadingUser, setLoadingUser) = useState { false }
	val (loadingPublications, setLoadingPublications) = useState { false }
	val (list, setList) = useState { arrayListOf<Publication>() }
	val userFilteredPublications by Store.publications.userFilteredPublications.collectAsState()
	val (isUserpublicationsFetched, setIsUserpublicationsFetched) = useState { false }
	val (isServerError, setIsServerError) = useState { false }


	fun fetchUserAndHisPublications() {
		setLoadingUser(true)
		Store.users.getCurrentActivated({
			setLoadingUser(false);
			setIsServerError(true);
		}) { _, _ ->
			setLoadingUser(false)
			setLoadingPublications(true)
			Store.publications.fetchUserPublications { array ->
				setList(array)
				setLoadingPublications(false)
				setIsUserpublicationsFetched(true)
			}
		}

	}

	LaunchedEffect(userFilteredPublications) {
		setIsServerError(false)
		if (isUserpublicationsFetched) {
			setList(Store.publications.userFilteredPublications.value)
			return@LaunchedEffect
		}
		fetchUserAndHisPublications()
	}

	// the user card is loading
	if (loadingUser) {
		Spinner()
		return
	}

	// the publications section is loading
	if (loadingPublications) {
		Column {
			UserCard()
			Line(height = 2.dp)
			Spinner()
		}
		return
	}

	// there a server error
	if (isServerError) {
		ComponentServerErrorMessage {
			fetchUserAndHisPublications()
		}
		return
	}

	LazyColumn(modifier = Modifier.padding(bottom = bottomBarHeight)) {
		item(key = "Page header") {
			UserCard()
			Line(height = 2.dp)
		}
		item {
			UserPublicationsTab()
		}
		if (list.size == 0) {
			item {
				Column(
					modifier = Modifier.padding(40.dp)
				) {
					EmptyPublicationsMessage()
				}
			}
		} else items(list, key = { it.id }) {
			Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp)) {
				PublicationArticle(it, true)
				Line(height = 1.dp)
			}
		}
	}

	BackHandler { if (!loadingUser) Router.navigate("home") }
}
