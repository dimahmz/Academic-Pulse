package com.example.academicpulse.view.pages.publication

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import com.example.academicpulse.view.components.basic.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.PdfViewer
import com.example.academicpulse.view_model.Store

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PDFViewerPage() {

	val (isLoading, setIsLoading) = useState { true }
	var (fileIsReady, setFileIsReady) = useState { false }
	var (fileUri, setFileUri) = useState<Uri?> { null }

	LaunchedEffect(true) {
		Store.files.readFile(Store.publications.selectedPublicationId, {}) {
			setFileIsReady(true)
			setFileUri(it!!)
		}
	}

	Box() {
		if (fileIsReady) {
			PdfViewer(modifier = Modifier.fillMaxSize(),
				pdfSource = fileUri as Object,
				loadingListener = { loading, currentPage, maxPage ->
					setIsLoading(loading)
				})
		}
		if (isLoading) {
			Column(
				modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
			) {
				Box(
					modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
				) {
					Spinner(size = 30.dp)
				}
				Text(
					modifier = Modifier
						.align(Alignment.CenterHorizontally)
						.padding(top = 5.dp)
						.padding(horizontal = 30.dp), text = R.string.file_is_loading
				)
			}
		}
	}
}
