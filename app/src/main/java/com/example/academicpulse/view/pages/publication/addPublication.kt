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
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Button
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store

@Composable
fun AddPublicationPage() {
	val form = useForm()
	val title = useField(form = form, ifEmpty = R.string.email_required)
	val abstract = useField(form = form, ifEmpty = R.string.abstract_required)
	val doi = useField(form = form, ifEmpty = R.string.abstract_required)

	val (loading, setLoading) = useState(false)

	fun addPublication() {
		if (loading || !form.validate()) return
		setLoading(true)
		Store.publications.insert(
			Publication(
				title = title.trim(),
				abstract = abstract.trim(),
				doi = doi.trim()
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
		Header(title = R.string.add_research)
		Spacer(Modifier.height(14.dp))

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
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
			)
		}
		Spacer(Modifier.height(14.dp))
		Button(
			text = R.string.add_research,
			height = 40.dp,
			loading = loading,
			onClick = ::addPublication
		)
	}

	BackHandler { Router.back() }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddPublicationPage() {
	AddPublicationPage()
}
