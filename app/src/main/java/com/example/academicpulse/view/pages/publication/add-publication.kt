package com.example.academicpulse.view.pages.publication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.inputLabelGap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.stringToDate
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.AuthorsRow
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store
import com.google.firebase.Timestamp

@Composable
fun AddPublicationPage() {
	val form = Store.publications.form
	val authors = useAtom(Store.authors.currentForm, arrayListOf())
	val typeOptions = useAtom(Store.publicationsTypes.list)
	val (typesFetched, setTypesFetched) = useState { false }
	val (loading, setLoading) = useState { false }

	// Fetch the available types
	LaunchedEffect(true) {
		Store.publicationsTypes.getAll(
			onError = { Router.back(navBarVisible = true) },
			onSuccess = { setTypesFetched(true) },
		)
	}

	fun addPublication() {
		if (loading || !form.form.validate()) return
		setLoading(true)
		val list = ArrayList<User>(authors.toMutableList())
		list.add(0, Store.user.current.value!!)
		Store.publications.insert(
			Publication(
				typeId = form.type.value,
				title = form.title.value,
				abstract = form.abstract.value,
				file = form.file.uri,
				doi = form.doi.value,
				date = Timestamp(stringToDate(form.date.value, "dd/MM/yyyy")),
				authors = list,
			)
		) { error ->
			form.form.error = error
			setLoading(false)
		}
	}

	if (!typesFetched) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 30.dp),
			contentAlignment = Alignment.Center
		) {
			Spinner(size = 30.dp)
		}
		return
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.add_research, true)
		Spacer(Modifier.height(14.dp))

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Select(
				field = form.type,
				label = R.string.type,
				items = typeOptions!!,
				getValue = { it.id },
				getLabel = { it.label },
			)
			Input(
				field = form.title,
				label = R.string.title,
				focusNext = form.abstract,
			)
			Input(
				field = form.abstract,
				label = R.string._abstract,
				focusNext = form.doi,
			)
			FilePicker(
				field = form.file,
				label = R.string.pdf_file,
				mimeTypes = arrayOf("application/pdf"),
				defaultFileName = "Article",
			)
			Input(
				field = form.doi,
				label = R.string.doi,
				focusNext = form.date,
			)
			DatePicker(field = form.date, label = R.string.date)
			Column {
				Row(modifier = Modifier.padding(bottom = inputLabelGap)) {
					Text(text = R.string.authors)
					Spacer(Modifier.width(inputLabelGap))
					Text(text = "(${stringResource(R.string.modify)})", underlined = true) {
						Router.navigate("publications/select-authors", false)
					}
				}
				AuthorsRow(authors = authors, appendCurrentUser = true)
			}
		}

		Spacer(Modifier.height((8 + gap.value).dp))
		form.form.Error()
		Spacer(Modifier.height(8.dp))
		Button(
			text = R.string.add_research, loading = loading, onClick = ::addPublication
		)
	}

	BackHandler {
		form.form.clearAll()
		Store.authors.currentForm.value = arrayListOf()
		Router.back(true /* to = profile/index */)
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddPublicationPage() {
	AddPublicationPage()
}
