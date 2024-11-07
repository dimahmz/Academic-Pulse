package com.example.academicpulse.model

import com.example.academicpulse.utils.useCast
import com.google.firebase.Timestamp
import java.util.Date

data class User(
	val id: String = "",
	var firstName: String = "",
	var lastName: String = "",
	var department: String = "",
	var position: String = "",
	var institution: String = "",
	var email: String = "",
	var activated: Boolean = false,
	var publications: ArrayList<String> = arrayListOf(),
	val createdAt: Timestamp = Timestamp(Date()),
) {
	val institutionSkipped: Boolean
		get() = institution.isBlank()

	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
			"firstName" to firstName,
			"lastName" to lastName,
			"institution" to (if (institutionSkipped) null else institution),
			"department" to (if (institutionSkipped) null else department),
			"position" to (if (institutionSkipped) null else position),
			"email" to email,
			"activated" to activated,
			"createdAt" to Timestamp(Date()),
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
				publications = useCast(map, "publications", arrayListOf()),
				createdAt = useCast(map, "createdAt", Timestamp(Date())),
			)
		}
	}
}
