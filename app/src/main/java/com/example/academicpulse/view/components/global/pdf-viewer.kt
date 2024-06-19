package com.example.academicpulse.view.components.global

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.annotation.RawRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*

enum class PdfListDirection {
	HORIZONTAL, VERTICAL
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
	@RawRes pdfResId: Int,
	modifier: Modifier = Modifier,
	pageColor: Color = Color.White,
	listDirection: PdfListDirection = PdfListDirection.VERTICAL,
	arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(16.dp),
	loadingListener: (
		isLoading: Boolean,
		currentPage: Int?,
		maxPage: Int?,
	) -> Unit = { _, _, _ -> }
) {
	val context = LocalContext.current
	PdfViewer(
		pdfSource = context.resources.openRawResource(pdfResId),
		modifier = modifier,
		pageColor = pageColor,
		listDirection = listDirection,
		arrangement = arrangement,
		loadingListener = loadingListener,
	)
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
	pdfSource: Any,
	modifier: Modifier = Modifier,
	pageColor: Color = Color.White,
	listDirection: PdfListDirection = PdfListDirection.VERTICAL,
	arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(6.dp),
	loadingListener: (
		isLoading: Boolean,
		currentPage: Int?,
		maxPage: Int?,
	) -> Unit = { _, _, _ -> }
) {
	PdfViewer(
		pdfSource = pdfSource,
		modifier = modifier,
		listDirection = listDirection,
		loadingListener = loadingListener,
		arrangement = arrangement
	) { lazyState, imagem ->
		PaginaPDF(
			imagem = imagem, lazyState = lazyState, backgroundColor = pageColor
		)
	}
}

@ExperimentalFoundationApi
@Composable
fun PdfViewer(
	pdfSource: Any,
	modifier: Modifier = Modifier,
	listDirection: PdfListDirection = PdfListDirection.VERTICAL,
	arrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(16.dp),
	loadingListener: (
		isLoading: Boolean,
		currentPage: Int?,
		maxPage: Int?,
	) -> Unit = { _, _, _ -> },
	page: @Composable (LazyListState, ImageBitmap) -> Unit
) {
	val context = LocalContext.current
	val pagePaths = remember {
		mutableStateListOf<String>()
	}
	LaunchedEffect(true) {
		if (pagePaths.isEmpty()) {
			val paths = context.loadPdf(pdfSource, loadingListener)
			pagePaths.addAll(paths)
		}
	}
	val lazyState = rememberLazyListState()
	when (listDirection) {
		PdfListDirection.HORIZONTAL -> LazyRow(
			state = lazyState, horizontalArrangement = arrangement
		) {
			items(pagePaths) { path ->
				var imageBitmap by remember {
					mutableStateOf<ImageBitmap?>(null)
				}
				LaunchedEffect(path) {
					imageBitmap = BitmapFactory.decodeFile(path).asImageBitmap()
				}
				imageBitmap?.let { page(lazyState, it) }
			}
		}

		PdfListDirection.VERTICAL -> LazyColumn(
			state = lazyState, verticalArrangement = arrangement
		) {
			items(pagePaths) { path ->
				var imageBitmap by remember {
					mutableStateOf<ImageBitmap?>(null)
				}
				LaunchedEffect(path) {
					imageBitmap = BitmapFactory.decodeFile(path).asImageBitmap()
				}
				imageBitmap?.let { page(lazyState, it) }
			}
		}
	}
}

@ExperimentalFoundationApi
@Composable
private fun PaginaPDF(
	imagem: ImageBitmap, lazyState: LazyListState, backgroundColor: Color = Color.White
) {
	Card(
		modifier = Modifier.background(backgroundColor),
	) {
		ZoomableImage(painter = BitmapPainter(imagem), scrollState = lazyState)
	}
}

suspend fun Context.loadPdf(
	pdfSource: Any, loadingListener: (
		isLoading: Boolean, currentPage: Int?, maxPage: Int?
	) -> Unit = { _, _, _ -> }
): List<String> = withContext(Dispatchers.Default) {
	loadingListener(true, null, null)
	val outputDir = cacheDir
	val tempFile = File.createTempFile("temp", "pdf", outputDir)
	tempFile.mkdirs()
	tempFile.deleteOnExit()

	when (pdfSource) {
		is InputStream -> {
			val outputStream = FileOutputStream(tempFile)
			pdfSource.copyTo(outputStream)
		}

		is Uri -> {
			contentResolver.openInputStream(pdfSource)?.use { inputStream ->
				val outputStream = FileOutputStream(tempFile)
				inputStream.copyTo(outputStream)
			}
		}

		else -> throw IllegalArgumentException("Unsupported PDF source type")
	}

	val input = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
	val renderer = PdfRenderer(input)
	(0 until renderer.pageCount).map { pageNumber ->
		loadingListener(true, pageNumber, renderer.pageCount)
		val file = File.createTempFile("PDFpage$pageNumber", "png", outputDir)
		file.mkdirs()
		file.deleteOnExit()
		val page = renderer.openPage(pageNumber)
		val bitmap = Bitmap.createBitmap(1240, 1754, Bitmap.Config.ARGB_8888)
		page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
		page.close()
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
		file.absolutePath
	}.also {
		loadingListener(false, null, renderer.pageCount)
		renderer.close()
	}
}


@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
	val buf = ByteArray(8192)
	var length: Int
	while (source.read(buf).also { length = it } > 0) {
		target.write(buf, 0, length)
	}
}