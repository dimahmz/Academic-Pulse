package com.example.academicpulse.view.pages.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.forms.Field
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.PublicationArticle
import com.example.academicpulse.view_model.Store

@Composable
fun HomePage() {
	val search = Field.use(form = null)
	val (loading, setLoading) = useState { true }
	val (list, setList) = useState { arrayListOf<Publication>() }

	LaunchedEffect(search.value.trim()) {
		setLoading(true)
		Store.publications.search(search.value) { array ->
			setList(array)
			setLoading(false)
		}
	}

	LazyColumn(Modifier.padding(bottom = bottomBarHeight)) {
		item(key = "Page header") {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.background(MaterialTheme.colorScheme.primary)
					.padding(vertical = 25.dp, horizontal = 15.dp),
			) {
				Input(
					field = search,
					icon = R.drawable.icon_search,
					placeholder = R.string.search,
				)
			}
			if (loading) Spinner()
		}

		if (!loading) {
			items(list, key = { it.id }) {
				Column(modifier = Modifier.padding(horizontal = (pagePaddingX.value / 2).dp)) {
					PublicationArticle(it)
					Line(height = 1.dp)
				}
			}
		}
	}

	BackHandler { Router.exit() }
}
