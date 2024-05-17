package com.example.academicpulse.view.pages.publication

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.PublicationType
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Button
import com.example.academicpulse.view.components.basic.DatePicker
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Select
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store
import com.google.firebase.Timestamp
import java.util.Date

@Composable
fun AddPublicationPage() {
	val form = useForm()
	val type = useField(form = form, ifEmpty = R.string.type_required)
	val title = useField(form = form, ifEmpty = R.string.title_required)
	val abstract = useField(form = form, ifEmpty = R.string.abstract_required)
	val doi = useField(form = form, required = false)
	val date = useField(form = form, ifEmpty = R.string.date_required)

	val (loading, setLoading) = useState { false }

	fun addPublication() {
		if (loading || !form.validate()) return
		setLoading(true)
		Store.publications.insert(
			Publication(
				typeId = type.value,
				title = title.trim(),
				abstract = abstract.trim(),
				doi = doi.trim(),
				date = Timestamp(Date(date.value))
			)
		) { error ->
			form.error = error
			setLoading(false)
		}
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
				field = type,
				label = R.string.type,
				items = PublicationType.list,
				getValue = { it.id },
				getLabel = { it.name },
				focusNext = title.focusRequester,
			)
			Input(
				field = title,
				label = R.string.title,
				focusNext = abstract.focusRequester,
			)
			Input(
				field = abstract,
				label = R.string._abstract,
				focusNext = doi.focusRequester,
			)
			Input(
				field = doi,
				label = R.string.doi,
				focusNext = date.focusRequester,
			)
			DatePicker(field = date, label = R.string.date)
		}

		Spacer(Modifier.height((8 + gap.value).dp))
		form.Error()
		Spacer(Modifier.height(8.dp))
		Button(
			text = R.string.add_research,
			loading = loading,
			onClick = ::addPublication
		)
	}

	BackHandler { Router.back(true /* to = profile/index */) }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddPublicationPage() {
	AddPublicationPage()
}
