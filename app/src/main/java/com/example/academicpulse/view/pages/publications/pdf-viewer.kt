package com.example.academicpulse.view.pages.publications

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.global.PdfViewer
import com.example.academicpulse.view_model.Store

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PDFViewerPage() {
	val (fileUri, setFileUri) = useState<Uri?> { null }

	LaunchedEffect(Unit) {
		Store.files.readFile(Store.publications.selectedPublicationId, {}) {
			try {
				setFileUri(it)
			} finally {
			}
		}
	}

	if (fileUri != null) {
		PdfViewer(modifier = Modifier.fillMaxSize(), source = fileUri)
	}

	BackHandler { Router.back("publications/one-publication") }
}
