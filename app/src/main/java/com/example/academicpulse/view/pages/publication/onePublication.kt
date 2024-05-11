package com.example.academicpulse.view.pages.publication

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Button
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store

@Composable
fun OnePublicationPage() {
	val publication = useAtom(Store.publications.selectedPublication, Publication())
	val (loading, setLoading) = useState { false }

	LaunchedEffect(true) {
		Store.publications.getOnePublicationById(
			"IYLG2z7rqMsctxjgo9Jm",
			onSuccess = {
				setLoading(false)
				Log.d("fetched pub", publication.toString())
			},
			onError = { setLoading(false) },
		)
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {

		Header(title = R.string.publication)
		Spacer(Modifier.height(14.dp))

		if (loading) {
			Box(
				modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
			) {
				Spinner(size = 64.dp)
			}
		} else {
			Text(text = "hello from publication")
		}
	}
	BackHandler { Router.back() }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewOnePublicationPage() {
	OnePublicationPage()
}
