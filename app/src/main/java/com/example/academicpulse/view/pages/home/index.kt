package com.example.academicpulse.view.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.pageWithBarPaddingBottom
import com.example.academicpulse.utils.forms.Field
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.ErrorMessage
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.PublicationArticle
import com.example.academicpulse.view_model.Store

@Composable
fun HomePage() {
	val (loading, setLoading) = useState { true }
	val (showError, setShowError) = useState { false }
	val publications = useAtom(Store.publications.filteredHomePublications, arrayListOf())

	LaunchedEffect(true) {
		Store.publications.fetchHomePublication(onSuccess = { setLoading(false) }) {
			setShowError(true)
			setLoading(false)
		}
	}
	LazyColumn(Modifier.padding(bottom = pageWithBarPaddingBottom)) {
		item(key = "Page header") {
			val search = Field.use(form = null)

			LaunchedEffect(search.value.trim()) {
				Store.publications.search(search.value) {
					Store.publications.filteredHomePublications.value = it
				}
			}

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

			Spacer(Modifier.height(15.dp))
			Line(height = 2.dp)
			if (loading) {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 30.dp),
					contentAlignment = Alignment.Center
				) {
					Spinner(size = 30.dp)
				}
			} else if (showError) ErrorMessage(errorMessage = R.string.unknown_error)
		}

		if (!loading && !showError) {
			items(publications, key = { it.id }) {
				Spacer(Modifier.height(15.dp))
				Column {
					PublicationArticle(it)
					Spacer(Modifier.height(10.dp))
					Line(height = 2.dp)
				}
			}
		}
	}
}
