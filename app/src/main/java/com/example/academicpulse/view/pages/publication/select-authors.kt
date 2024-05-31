package com.example.academicpulse.view.pages.publication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.H3
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.AuthorsRow
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view_model.Store

@Composable
fun SelectAuthorsPage() {
	val search = useField(form = useForm())
	val (loading, setLoading) = useState { true }
	val (list, setList) = useState { Store.publications.currentFormAuthors }

	fun onFinish() {
		Store.publications.currentFormAuthors = arrayListOf()
		Router.back(false)
	}

	LaunchedEffect(search.value.trim()) {
		logcat(search.value.trim())
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 10.dp, bottom = 6.dp),
			contentAlignment = Alignment.CenterStart
		) {
			H3(
				text = R.string.authors,
				modifier = Modifier.fillMaxWidth(),
				align = TextAlign.Center
			)

			Row(Modifier.fillMaxWidth()) {
				Spacer(Modifier.weight(1f))
				Text(text = R.string.continued, onClick = ::onFinish)
			}
		}
		Line(height = 1.dp)
		Spacer(Modifier.height(10.dp))
		AuthorsRow(authors = list, appendCurrentUser = true) {
			logcat(it.id)
		}
		Input(
			field = search,
			icon = R.drawable.icon_search,
			placeholder = R.string.search,
			outlined = false,
		)
		Line(height = 1.dp)
	}
//	LazyColumn(Modifier.padding(bottom = pageWithBarPaddingBottom)) {
//		if (loading) {
//			item(key = "Page header") {
//				Box(
//					modifier = Modifier
//						.fillMaxWidth()
//						.padding(top = 30.dp),
//					contentAlignment = Alignment.Center
//				) {
//					Spinner(size = 30.dp)
//				}
//			}
//		} else {
//			items(publications, key = { it.id }) {
//				Spacer(Modifier.height(15.dp))
//				Column {
//					PublicationArticle(it)
//					Spacer(Modifier.height(10.dp))
//					Line(height = 2.dp)
//				}
//			}
//		}
//	}
	BackHandler(onBack = ::onFinish)
}
