package com.example.academicpulse.model

import com.example.academicpulse.utils.useCast

data class Publication(
	val id: String = "",
	var title: String = "",
	var abstract: String = "",
	var doi: String = "",
	var date: String = "",
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
		fun fromMap(map: Map<String, Any?>?): Publication {
			return Publication(
				id = useCast(map, "id", ""),
				title = useCast(map, "title", ""),
				abstract = useCast(map, "abstract", ""),
				doi = useCast(map, "doi", ""),
				date = useCast(map, "date", ""),
				reads = useCast(map, "reads",  0),
				uploads = useCast(map, "uploads",  0),
			)
		}
	}
}
