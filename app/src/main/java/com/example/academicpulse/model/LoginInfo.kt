package com.example.academicpulse.model

class LoginInfo {
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

	companion object {
		fun fromMap(map: MutableMap<String, Any>): Pair<SignUpInfo, Boolean> {
			val signUpInfo = SignUpInfo()
			signUpInfo.email = map["email"].toString()
			signUpInfo.firstName = map["firstName"].toString()
			signUpInfo.lastName = map["lastName"].toString()
			signUpInfo.institution = map["institution"]?.toString() ?: ""
			signUpInfo.department = map["department"]?.toString() ?: ""
			signUpInfo.position = map["position"]?.toString() ?: ""
			signUpInfo.institutionSkipped = (map["institution"] == null)
			return Pair(signUpInfo, map["activated"] as Boolean)
		}
	}
}
