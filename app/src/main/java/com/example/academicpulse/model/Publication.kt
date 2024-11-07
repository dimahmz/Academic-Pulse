package com.example.academicpulse.model

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.example.academicpulse.utils.useCast
import com.example.academicpulse.view_model.Store
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Publication(
	var id: String = "",
	val typeId: String,
	val title: String,
	val abstract: String,
	file: Uri? = null,
	val doi: String,
	val date: Timestamp,
	val authors: List<User>,
	val authorID: String,
	val reads: Long = 0,
	val uploads: Long = 0,
	val status: String = "pending",
	val hasFile: Boolean = false,
	val createdAt: Timestamp = Timestamp(Date()),
) {
	private var typeLabel: String? = null
	private var _file = mutableStateOf(file)
	private val _fileAvailability = mutableStateOf(false)

	val type: String
		get() {
			if (typeLabel == null) {
				val type = Store.publicationsTypes.getOne(typeId)
				if (type != null) typeLabel = type.label
			}
			return typeLabel ?: "Article"
		}
	val fileAvailability: Boolean
		get() = _fileAvailability.value
	val file: Uri?
		get() = _file.value

	fun fetchFile() {
		if (_file.value != null) {
			if (!_fileAvailability.value) _fileAvailability.value = true
		} else Store.files.readFile(id, { _fileAvailability.value = true }) {
			if (it != null) _file.value = it
			_fileAvailability.value = true
		}
	}

	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
			"typeId" to typeId,
			"title" to title,
			"abstract" to abstract,
			"doi" to doi,
			"date" to date,
			"reads" to reads,
			"uploads" to uploads,
			"authors" to authors.map { it.id },
			"authorID" to authorID,
			"status" to status,
			"createdAt" to Timestamp(Date()),
			"hasFile" to hasFile
		)
	}

	companion object {
		fun fromMap(id: String, map: Map<String, Any?>?, authors: List<User>): Publication {
			return Publication(
				id = id,
				typeId = useCast(map, "typeId", ""),
				title = useCast(map, "title", ""),
				abstract = useCast(map, "abstract", ""),
				doi = useCast(map, "doi", ""),
				date = useCast(map, "date", Timestamp(Date())),
				authors = authors,
				authorID = useCast(map, "authorID", ""),
				reads = useCast(map, "reads", 0),
				uploads = useCast(map, "uploads", 0),
				status = useCast(map, "status", "pending"),
				createdAt = useCast(map, "createdAt", Timestamp(Date())),
				hasFile = useCast(map, "hasFile", false),
			)
		}

		fun formatDate(date: Timestamp): String {
			val date2 = Date(date.seconds * 1000L + date.nanoseconds / 1000000L)
			val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
			return sdf.format(date2)
		}
	}
}
