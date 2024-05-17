package com.example.academicpulse.model

data class PublicationType(val id: String, val name: String) {
	companion object {
		val list = arrayListOf(
			PublicationType("ar", "Arabic"),
			PublicationType("en", "English"),
			PublicationType("de", "Dutch"),
			PublicationType("es", "Spanish"),
			PublicationType("fr", "French"),
		)
	}
}
