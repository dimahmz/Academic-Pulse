package com.example.academicpulse.view.pages.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
	val (loading, setLoading) = useState(true)
	val publications = useAtom(Store.publications.userPublications)

	LaunchedEffect(true) {
		Store.publications.getUserPublications(
			onSuccess = { setLoading(false) },
			onError = { setLoading(false) },
		)
	}

	LazyColumn(modifier = Modifier.padding(bottom = bottomBarHeight)) {
		item {
			UserCard { Router.navigate("profile/add-publication", false) }
			Line(height = 2.dp)
			if (loading) Spinner(size = 64.dp)
		}

		if (!loading && publications != null)
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
