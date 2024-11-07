package com.example.academicpulse.view.pages.publications

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.PublicationType
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
	val authors = useAtom(form.authors, arrayListOf())
	val (types, setTypes) = useState<List<PublicationType>?> { null }
	val (loading, setLoading) = useState { false }

	// Fetch the available types
	LaunchedEffect(Unit) {
		Store.publicationsTypes.getAll(
			onError = { Router.back("profile") },
			onSuccess = { setTypes(it) },
		)
	}

	fun addPublication() {
		if (loading || !form.form.validate()) return
		setLoading(true)
		val list = ArrayList<User>(authors.toMutableList())
		list.add(0, Store.users.current.value!!)
		Store.publications.insert(
			Publication(
				typeId = form.type.value,
				title = form.title.value,
				abstract = form.abstract.value,
				file = form.file.uri,
				doi = form.doi.value,
				date = Timestamp(stringToDate(form.date.value, "dd/MM/yyyy")),
				authors = list,
				hasFile = form.file.uri != null,
				authorID = Store.users.current.value!!.id
			)
		) { error ->
			form.form.error = error
			setLoading(false)
		}
	}

	val (back, setBack) = useState { {} }
	LaunchedEffect(loading) {
		if (loading) setBack {}
		else setBack {
			form.form.clearAll()
			form.authors.value = arrayListOf()
			Router.back("profile")
		}
	}

	if (types == null) {
		Spinner()
		return
	}

	LazyColumn(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		item {

			Header(title = R.string.add_research, onClick = back)
			Spacer(Modifier.height(14.dp))

			Column(verticalArrangement = Arrangement.spacedBy(gap)) {
				Select(
					field = form.type,
					label = R.string.type,
					items = types,
					getValue = { it.id },
					getLabel = { it.label },
					icon = R.drawable.icon_down_arrow
				)
				Input(
					field = form.title, label = R.string.title, focusNext = form.abstract, height = 50.dp
				)
				Input(
					field = form.abstract, label = R.string._abstract, focusNext = form.doi, height = 140.dp
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
						if (loading) Text(text = "(${stringResource(R.string.modify)})", underlined = true)
						else Text(text = "(${stringResource(R.string.modify)})", underlined = true) {
							Router.navigate("publications/select-authors")
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
			Spacer(Modifier.height(20.dp))
		}
	}
}
