package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academicpulse.model.LoginInfo
import com.example.academicpulse.model.SignUpInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class AuthViewModel : ViewModel() {
	val loginInfo: LoginInfo = LoginInfo()
	val signUpInfo: SignUpInfo = SignUpInfo()

	fun saveLoginInfo(email: String, password: String) {
		loginInfo.email = email
		loginInfo.password = password
	}

	fun saveInstitutionInfo(institution: String, department: String, position: String, skipped: Boolean) {
		signUpInfo.institution = institution
		signUpInfo.department = department
		signUpInfo.position = position
		signUpInfo.institutionSkipped = skipped
	}

	fun saveUserInfo(firstName: String, lastName: String, email: String, password: String) {
		signUpInfo.firstName = firstName
		signUpInfo.lastName = lastName
		signUpInfo.email = email
		signUpInfo.password = password
	}

	fun setConfirmationCode(value: String) {
		signUpInfo.code = value
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

	fun login(onSuccess: (Boolean) -> Unit) {
		val email = loginInfo.email
		val password = loginInfo.password

		// Lunch a separated async script to log in
		viewModelScope.launch {
			delay(2000L)
			val loggedIn = Random.nextBoolean()
			onSuccess(loggedIn)
		}
	}

	fun signup(onSuccess: (Boolean) -> Unit) {
		val firstName = signUpInfo.firstName
		val lastName = signUpInfo.lastName
		val email = signUpInfo.email
		val password = signUpInfo.password
		val skipped = signUpInfo.institutionSkipped
		val institution = if (skipped) null else signUpInfo.institution
		val department = if (skipped) null else signUpInfo.department
		val position = if (skipped) null else signUpInfo.position

		// Lunch a separated async script to sign up
		viewModelScope.launch {
			delay(2000L)
			setConfirmationCode("1443")
			val emailAlreadyExist = Random.nextBoolean()
			onSuccess(emailAlreadyExist)
		}
	}
}
