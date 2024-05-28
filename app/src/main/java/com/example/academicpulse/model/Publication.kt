package com.example.academicpulse.model

import com.example.academicpulse.utils.useCast
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Publication(
	val id: String = "",
	val typeId: String,
	val title: String,
	val abstract: String,
	val doi: String,
	val date: Timestamp,
	val reads: Long = 0,
	val uploads: Long = 0,
	var type: String = ""
) {
	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
			"typeId" to typeId,
			"title" to title,
			"abstract" to abstract,
			"doi" to doi,
			"date" to date,
			"reads" to reads,
			"uploads" to uploads,
		)
	}
	companion object {
		fun fromMap(id: String, map: Map<String, Any?>?): Publication {
			return Publication(
				id = id,
				typeId = useCast(map, "typeId", ""),
				title = useCast(map, "title", ""),
				abstract = useCast(map, "abstract", ""),
				doi = useCast(map, "doi", ""),
				date = useCast(map, "date", Timestamp(Date())),
				reads = useCast(map, "reads", 0),
				uploads = useCast(map, "uploads", 0),
			)
		}
		fun formatDate(date: Timestamp): String {
			val date2 = Date(date.seconds * 1000L + date.nanoseconds / 1000000L)
			val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
			return sdf.format(date2)
		}
	}
}
