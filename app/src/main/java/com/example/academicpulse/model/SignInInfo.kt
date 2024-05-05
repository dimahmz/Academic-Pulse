package com.example.academicpulse.model

class SignInInfo {
	var email: String = ""
	var password: String = ""
}

class SignUpInfo {
	var institution: String = ""
	var department: String = ""
	var position: String = ""
	var firstName: String = ""
	var lastName: String = ""
	var email: String = ""
	var password: String = ""
	var institutionSkipped: Boolean = false

	fun toMap(uid: String): HashMap<String, Any?> {
		return hashMapOf(
			"uid" to uid,
			"email" to email,
			"firstName" to firstName,
			"lastName" to lastName,
			"institution" to (if (institutionSkipped) null else institution),
			"department" to (if (institutionSkipped) null else department),
			"position" to (if (institutionSkipped) null else position),
			"role" to "user",
			"activated" to false
		)
	}
}
