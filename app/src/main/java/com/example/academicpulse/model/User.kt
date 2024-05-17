package com.example.academicpulse.model

import com.example.academicpulse.utils.useCast

data class User(
	val id: String = "",
	var firstName: String = "",
	var lastName: String = "",
	var department: String = "",
	var position: String = "",
	var institution: String = "",
	var email: String = "",
	var activated: Boolean = false,
	var institutionSkipped: Boolean = false
) {
	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
			"firstName" to firstName,
			"lastName" to lastName,
			"institution" to (if (institutionSkipped) null else institution),
			"department" to (if (institutionSkipped) null else department),
			"position" to (if (institutionSkipped) null else position),
			"email" to email,
			"activated" to activated,
		)
	}

	companion object {
		fun fromMap(id: String, map: Map<String, Any?>?): User {
			val institution = useCast(map, "institution", "")
			return User(
				id = id,
				firstName = useCast(map, "firstName", ""),
				lastName = useCast(map, "lastName", ""),
				department = useCast(map, "department", ""),
				position = useCast(map, "position", ""),
				institution = institution,
				email = useCast(map, "email", ""),
				activated = useCast(map, "activated", false),
				institutionSkipped = institution == "",
			)
		}
	}
}
