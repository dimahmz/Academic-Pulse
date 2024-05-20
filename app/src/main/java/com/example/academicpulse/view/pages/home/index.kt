package com.example.academicpulse.view.pages.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.theme.pageWithBarPaddingBottom
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.FetchErrorMessage
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.components.global.PublicationArticle
import com.example.academicpulse.view_model.Store

@Composable
fun HomePage() {
	val (loading, setLoading) = useState { true }
	val (showError, setShowError) = useState { false }
	val publications = useAtom(Store.publications.homePublications, arrayListOf())

	LaunchedEffect(true) {
		Store.publications.fetchHomePublication(onSuccess = {
			setLoading(false)
			logcat("publications : $publications")
		}) {
			setShowError(true)
			setLoading(false)
		}
	}
	LazyColumn(Modifier.padding(bottom = pageWithBarPaddingBottom)) {
		item(key = "Page header") {
			HomeHeader()
			Spacer(Modifier.height(15.dp))
			Line(height = 2.dp)
		}
		if (loading) {
			item(key = "Loader") {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 30.dp),
					contentAlignment = Alignment.Center
				) {
					Spinner(size = 30.dp)
				}
			}
		} else if (showError) {
			item(key = "error card") {
				FetchErrorMessage(errorMessage = "An error has occurred")
			}
		} else {
			items(publications, key = { it.id }) {
				Spacer(Modifier.height(15.dp))
				Column() {
					PublicationArticle(it)
					Spacer(Modifier.height(10.dp))
					Line(height = 2.dp)
				}
			}
		}
	}
}