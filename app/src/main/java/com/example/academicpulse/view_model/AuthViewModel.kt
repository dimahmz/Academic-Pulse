package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.LoginInfo
import com.example.academicpulse.model.SignUpInfo
import com.example.academicpulse.utils.logcat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class AuthViewModel : ViewModel() {

	// Firebase Authentication instance
	private val auth: FirebaseAuth = Firebase.auth

	val loginInfo: LoginInfo = LoginInfo()
	val signUpInfo: SignUpInfo = SignUpInfo()
	private var institutionSkipped: Boolean = false

	fun saveLoginInfo(email: String, password: String) {
		loginInfo.email = email
		loginInfo.password = password
	}

	fun saveInstitutionInfo(
		institution: String, department: String, position: String, skipped: Boolean
	) {
		signUpInfo.institution = institution
		signUpInfo.department = department
		signUpInfo.position = position
		institutionSkipped = skipped
	}

	fun saveSignUpInfo(firstName: String, lastName: String, email: String, password: String) {
		signUpInfo.firstName = firstName
		signUpInfo.lastName = lastName
		signUpInfo.email = email
		signUpInfo.password = password
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
	}

	fun login(callback: (Boolean, String) -> Unit) {
		val email = loginInfo.email
		val password = loginInfo.password
		auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
			val user = auth.currentUser
			// If any unexpected error occurred.
			if (user == null || !login.isSuccessful) {
				callback(false, "An error has occurred, please try again later")
				logcat("signInWithEmail:failure", login.exception)
				return@addOnCompleteListener
			}
			// Check if the email is verified
			if (!user.isEmailVerified) {
				callback(false, "Must verify User before Login")
				return@addOnCompleteListener
			}
			// TODO: Check if the user account is activated
			callback(true, "")
		}
	}

	fun signup(callback: (Boolean, String) -> Unit) {
		val firstName = signUpInfo.firstName
		val lastName = signUpInfo.lastName
		val email = signUpInfo.email
		val password = signUpInfo.password
		val institution = if (institutionSkipped) null else signUpInfo.institution
		val department = if (institutionSkipped) null else signUpInfo.department
		val position = if (institutionSkipped) null else signUpInfo.position

		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { creating ->
			val user = auth.currentUser
			// Exit if the user is null or anything went wrong
			if (user == null || !creating.isSuccessful) {
				callback(false, creating.exception?.message ?: "An unknown error occurred")
				logcat("Error creating user:", creating.exception)
				return@addOnCompleteListener
			}

			// create a new user collection and fill its content
			val userRef = Store.database.collection("users").document(user.uid)
			val userData = hashMapOf(
				"uid" to user.uid,
				"email" to email,
				"firstName" to firstName,
				"lastName" to lastName,
				"institution" to institution,
				"department" to department,
				"position" to position,
				"role" to "user",
				"activated" to false
			)
			userRef.set(userData).addOnCompleteListener { saving ->
				if (!saving.isSuccessful) {
					logcat("Error creating user document: ", saving.exception)
					callback(false, saving.exception?.message ?: "An unknown error occurred")
				} else {
					// Send a verification email to the user
					user.sendEmailVerification().addOnCompleteListener { sending ->
						if (sending.isSuccessful) {
							callback(true, "Account has been created")
							logcat("Email sent.")
						} else callback(false, sending.exception?.message ?: "An unknown error occurred")
					}
				}
			}
		}
	}
}
