package com.example.academicpulse.model

import com.example.academicpulse.model.DB.Author

class AddPublicationInfo {
	var abstract: String =  ""
	var title: String =  ""
	var doi: String = ""
	var uid: String = ""
	var date: String = ""
	var reads: Int = 12
	var uploads: Int = 2

	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
			"uid" to uid,
			"abstract" to abstract,
			"title" to title,
			"doi" to doi,
			"date" to date,
			"reads" to reads,
			"uploads" to uploads,
		)
	}
}
