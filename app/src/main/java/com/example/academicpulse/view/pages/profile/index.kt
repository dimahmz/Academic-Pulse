package com.example.academicpulse.view.pages.profile

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.example.academicpulse.model.DB.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.gap
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
	val profile = Store.profile
	val publications = useAtom(profile.userPublications)
	val (loading, setLoading) = useState(true)

	fun getUserPublication() {
		profile.fetchUserPublication { message, success ->
			setLoading(false)
		}
	}

	fun addPublication() {
		Router.navigate("profile/add-publication", navBarVisible = false)
	}

	// Execute  when ProfilePage is rendered
	LaunchedEffect(true) {
		getUserPublication()
	}

	LazyColumn(modifier = Modifier.padding(bottom = bottomBarHeight)) {
		item {
			UserCard(onAddResearch = ::addPublication)
			Line(height = 2.dp)
			if (loading) {
				Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
					Spinner(modifier = Modifier.width(64.dp))
				}

			}
		}

		if (!loading) {
			if (publications != null) {
				items(publications) {
					Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp)) {
						Log.d("publication", it.toString())
						PublicationArticle(it)
						Line(height = 1.dp)
					}
				}
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
