package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.SignUpInfo

class AuthViewModel : ViewModel() {
	val signUpInfo: SignUpInfo = SignUpInfo()

	fun saveInstitutionInfo(institution: String, department: String, position: String) {
		signUpInfo.institution = institution
		signUpInfo.department = department
		signUpInfo.position = position
	}

	fun saveUserInfo(firstName: String, lastName: String, email: String, password: String) {
		signUpInfo.firstName = firstName
		signUpInfo.lastName = lastName
		signUpInfo.email = email
		signUpInfo.password = password
	}

	fun clearSignUp() {
		signUpInfo.email = ""
		signUpInfo.password = ""
		signUpInfo.firstName = ""
		signUpInfo.lastName = ""
		signUpInfo.code = ""
	}

	fun setVerificationCode(code: String) {
		signUpInfo.code = code
	}
}
