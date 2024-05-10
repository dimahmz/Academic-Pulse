package com.example.academicpulse.view.pages.publication

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
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

	val publication = Store.publication

	val newPublicationInfo = publication.addPubInfo

	val form = useForm()
	val title = useField(
		form = form,
		value = publication.addPubInfo.title,
		ifEmpty = R.string.email_required,
	)
	val abstract = useField(
		form = form,
		value = publication.addPubInfo.abstract,
		ifEmpty = R.string.abstract_required,
	)
	val doi = useField(
		form = form,
		value = publication.addPubInfo.doi,
		ifEmpty = R.string.abstract_required,
	)

	val (loading, setLoading) = useState(false)

	fun addPublication() {
		if (loading) return
		if (form.validate()) {
			setLoading(true)
			publication.saveNewPubInfo(title, abstract, doi)
			publication.addNewPub { msg, isSucceed ->
				if (!isSucceed) {
					Log.d("adding publication", "an error has occurred")
					form.error = msg
					// TODO
					// a modal should be rendered to the user
				}
				setLoading(false)
			}

		} else form.focusOnFirstInvalidField()

	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.add_research)
		Spacer(Modifier.height(14.dp))
		Column(verticalArrangement = Arrangement.spacedBy(gap)) {

		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Input(
				field = title,
				label = R.string.title,
				focusNext = abstract.focusRequester,
				keyboardType = KeyboardType.Text,
			)
			Input(
				field = abstract,
				label = R.string._abstract,
				keyboardType = KeyboardType.Text,
			)
			Input(
				field = doi,
				label = R.string.doi,
				keyboardType = KeyboardType.Text,
			)
		}
		Spacer(Modifier.height(14.dp))
		Button(
			text = R.string.add_research,
			height = 40.dp,
			loading = loading,
		) {
			addPublication()
		}

	}


	BackHandler { Router.back() }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAddPublicationPage() {
	AddPublicationPage()
}
