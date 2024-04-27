package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.LoginInfo
import com.example.academicpulse.model.SignUpInfo

class AuthViewModel : ViewModel() {
	private val loginInfo: LoginInfo = LoginInfo()
	val signUpInfo: SignUpInfo = SignUpInfo()

	fun saveLoginInfo(email: String, password: String) {
		loginInfo.email = email
		loginInfo.password = password
	}

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

	fun setVerificationCode(code: String) {
		signUpInfo.code = code
	}

	fun clearLogin() {
		loginInfo.email = ""
		loginInfo.password = ""
	}

	fun clearSignUp() {
		signUpInfo.email = ""
		signUpInfo.password = ""
		signUpInfo.firstName = ""
		signUpInfo.lastName = ""
		signUpInfo.code = ""
	}
}
