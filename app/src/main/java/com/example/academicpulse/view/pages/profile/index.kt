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
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.pagePaddingX
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
	val (list, setList) = useState { arrayListOf<Publication>() }

	LaunchedEffect(Unit) {
		Store.users.getCurrentActivated({ setLoadingUser(false); Router.navigate("auth/sign-in"); }) { _, _ ->
			setLoadingUser(
				false
			)
		}
		Store.publications.fetchUserPublications { array ->
			try {
				setList(array)
				setLoadingPublications(false)
			} finally {
			}
		}
	}

	if (loadingUser) {
		Spinner()
	} else if (loadingPublications) {
		Column {
			UserCard()
			Line(height = 2.dp)
			Spinner()
		}
	} else {
		LazyColumn(modifier = Modifier.padding(bottom = bottomBarHeight)) {
			item(key = "Page header") {
				UserCard()
				Line(height = 2.dp)
			}
			items(list, key = { it.id }) {
				Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp)) {
					PublicationArticle(it, true)
					Line(height = 1.dp)
				}
			}
		}
	}
	BackHandler { if (!loadingUser) Router.navigate("home") }
}
