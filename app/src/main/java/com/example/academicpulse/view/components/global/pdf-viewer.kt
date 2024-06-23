package com.example.academicpulse.view.components.global

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import com.example.academicpulse.theme.white
import com.example.academicpulse.utils.useState

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
	modifier: Modifier = Modifier,
	source: Uri,
	arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(16.dp),
) {
	val (images) = useState {
		val renderer = PdfRenderer(
			ParcelFileDescriptor.open(source.toFile(), ParcelFileDescriptor.MODE_READ_ONLY)
		)
		val list = ((0 until renderer.pageCount).map { pageNumber ->
			val page = renderer.openPage(pageNumber)
			val bitmap = Bitmap.createBitmap(1240, 1754, Bitmap.Config.ARGB_8888)
			page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
			page.close()
			bitmap.asImageBitmap()
		})
		renderer.close()
		list
	}

	val lazyState = rememberLazyListState()
	LazyColumn(modifier = modifier, state = lazyState, verticalArrangement = arrangement) {
		items(images) {
			Image(
				modifier = Modifier.background(white),
				painter = BitmapPainter(it),
				contentDescription = null
			)
		}
	}
}
