package com.example.academicpulse.view.pages.publication

import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.stringToDate
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Button
import com.example.academicpulse.view.components.basic.DatePicker
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Select
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store
import com.google.firebase.Timestamp
import com.google.type.Date

@Composable
fun AddPublicationPage() {
	val form = useForm()
	val type = useField(form = form, ifEmpty = R.string.type_required)
	val title = useField(form = form, ifEmpty = R.string.title_required)
	val abstract = useField(form = form, ifEmpty = R.string.abstract_required)
	val doi = useField(form = form, required = false)
	val date = useField(form = form, ifEmpty = R.string.date_required)
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
		if (loading || !form.validate()) return
		setLoading(true)
		logcat("date ${date.value}")
		Store.publications.insert(
			Publication(
				typeId = type.value,
				title = title.trim(),
				abstract = abstract.trim(),
				doi = doi.trim(),
				date = Timestamp(stringToDate(date.value, "dd/MM/yyyy"))
			)
		) { error ->
			form.error = error
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
			if (typeOptions != null)
				Select(
					field = type,
					label = R.string.type,
					items = typeOptions,
					getValue = { it.id },
					getLabel = { it.label },
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
			text = R.string.add_research, loading = loading, onClick = ::addPublication
		)
	}

	BackHandler { Router.back(true /* to = profile/index */) }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddPublicationPage() {
	AddPublicationPage()
}
