package com.example.academicpulse.model.DB

data class Publication(
	val uid: String,
	val title: String,
	val abstract: String,
	val date: String,
	val reads: Number,
	val uploads: Number,
) {
	companion object {
		fun fromMap(map: Map<String, Any?>?): Publication {
			val uid = map?.get("uid") as? String? ?: ""
			val title = map?.get("title") as? String? ?: ""
			val abstract = map?.get("abstract") as? String? ?: ""
			val date = map?.get("date") as? String? ?: ""
			val reads = map?.get("reads") as? Number ?: 0
			val uploads = map?.get("uploads") as? Number ?: 0

			return Publication(uid, title, abstract, date, reads, uploads)
		}
	}
}

