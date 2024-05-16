package com.example.academicpulse.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.academicpulse.R
import com.example.academicpulse.utils.useCast
import com.google.firebase.Timestamp
import java.util.Date

data class User(
	val id: String = "",
	val firstName: String = "",
	val lastName: String = "",
	val department: String = "",
	val position: String = "",
	val institution: String = "",
	val email: String = "",
) {


	fun toMap(): HashMap<String, Any?> {
		return hashMapOf(
			"id" to id,
			"firstName" to firstName,
			"lastName" to lastName,
			"department" to department,
			"position" to position,
			"institution" to institution,
			"email" to email,
		)
	}

	companion object {
		fun fromMap(id: String, map: Map<String, Any?>?): User {
			return User(
				id = id,
				firstName = useCast(map, "firstName", "firstName"),
				lastName = useCast(map, "lastName", "lastName"),
				department = useCast(map, "department", "department"),
				position = useCast(map, "position", "position"),
				institution = useCast(map, "institution", "institution"),
				email = useCast(map, "email", "")
			)
		}
	}
}