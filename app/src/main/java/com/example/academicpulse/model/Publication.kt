package com.example.academicpulse.model

import com.example.academicpulse.utils.useCast
import com.google.firebase.Timestamp
import java.util.Date

data class Publication(
	val id: String = "",
	var title: String = "",
	var abstract: String = "",
	var doi: String = "",
	var date: Timestamp,
	var reads: Long = 0,
	var uploads: Long = 0,
) {
	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
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
				title = useCast(map, "title", ""),
				abstract = useCast(map, "abstract", ""),
				doi = useCast(map, "doi", ""),
				date = useCast(map, "date", Timestamp(Date())),
				reads = useCast(map, "reads",  0),
				uploads = useCast(map, "uploads",  0),
			)
		}
	}
}
