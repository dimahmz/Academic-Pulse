package com.example.academicpulse.view.pages.publication

import androidx.activity.compose.BackHandler
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
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store

@Composable
fun OnePublicationPage() {
	val (loading, setLoading) = useState { true }
	val publication = useAtom(Store.publications.publication)
	val id = Store.publications.clickedPublicationID

	LaunchedEffect(true) {

		Store.publications.fetchOneById(id = id, onSuccess = { setLoading(false) }) {
			setLoading(false)
		}

	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.publication, true)
		Spacer(Modifier.height(14.dp))

		if (loading) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 30.dp), contentAlignment = Alignment.Center
			) {
				Spinner(size = 30.dp)
			}
		} else if (publication != null) {
			Text(text = "$publication")
		}
	}
	BackHandler { Router.back(true /* to = profile/index */) }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewOnePublicationPage() {
	OnePublicationPage()
}
