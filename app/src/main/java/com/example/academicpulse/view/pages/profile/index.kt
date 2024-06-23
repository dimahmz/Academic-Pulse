package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.UserCard
import com.example.academicpulse.view.components.global.PublicationArticle
import com.example.academicpulse.view_model.Store

@Composable
fun ProfilePage() {
	val (loadingUser, setLoadingUser) = useState { true }
	val (loadingPublications, setLoadingPublications) = useState { true }
	val publications = useAtom(Store.publications.userPublications, arrayListOf())

	LaunchedEffect(Unit) {
		Store.users.getCurrentUser({}) { _, _ -> setLoadingUser(false) }
		Store.publications.fetchUserPublications(onSuccess = { setLoadingPublications(false) }) {
			setLoadingPublications(false)
		}
	}

	if (loadingUser) {
		Spinner()
		return
	}

	LazyColumn(modifier = Modifier.padding(bottom = bottomBarHeight)) {
		item(key = "Page header") {
			UserCard()
			Line(height = 2.dp)
			if (loadingPublications) Spinner()
		}

		if (!loadingPublications) {
			items(publications, key = { it.id }) {
				Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp)) {
					PublicationArticle(it, true)
					Line(height = 1.dp)
				}
			}
		}
	}

	BackHandler { Router.navigate("home") }
}
