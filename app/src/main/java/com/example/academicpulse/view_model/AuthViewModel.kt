package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.LoginInfo
import com.example.academicpulse.model.SignUpInfo
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.Firebase

class AuthViewModel : ViewModel() {

	// Firebase Authentication instance
	private val auth: FirebaseAuth = Firebase.auth

	val loginInfo: LoginInfo = LoginInfo()
	val signUpInfo: SignUpInfo = SignUpInfo()

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
		signUpInfo.institutionSkipped = skipped
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

	fun login(onError: (Int) -> Unit) {
		val email = loginInfo.email
		val password = loginInfo.password
		auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { login ->
			val user = auth.currentUser
			// Exit if the user is null or anything went wrong
			if (user == null || !login.isSuccessful) {
				onError(R.string.unkown_error)
				logcat("Error sign in:", login.exception)
				return@addOnCompleteListener
			}

			// Check if the email is verified
			if (!user.isEmailVerified) {
				onError(R.string.verify_email_first)
				return@addOnCompleteListener
			}

			// Check if the user account is activated
			val userRef = Store.database.collection("user").document(user.uid)
			userRef.get().addOnCompleteListener { getUser ->
				val data = getUser.result.data
				if (data == null || !getUser.isSuccessful) {
					onError(R.string.unkown_error)
					logcat("Error read user document:", getUser.exception)
				} else {
					clearLogin()
					clearSignUp()
					val (info, activated) = SignUpInfo.fromMap(data)
					if (activated) Router.replace("home", true)
					else logcat("Account of {${info.firstName}} is not activated")
				}
			}
		}
	}

	fun signup(onError: (Int) -> Unit) {
		val email = signUpInfo.email
		val password = signUpInfo.password

		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { creating ->
			val user = auth.currentUser
			// Exit if the user is null or anything went wrong
			if (user == null || !creating.isSuccessful) {
				logcat("Error creating user:", creating.exception)
				onError(R.string.unkown_error)
				return@addOnCompleteListener
			}

			// create a new user collection and fill its content
			val userRef = Store.database.collection("user").document(user.uid)
			userRef.set(signUpInfo.toMap(user.uid)).addOnCompleteListener { saving ->
				if (!saving.isSuccessful) {
					logcat("Error creating user document: ", saving.exception)
					onError(R.string.unkown_error)
				} else {
					// Send a verification email to the user
					user.sendEmailVerification().addOnCompleteListener { sending ->
						if (sending.isSuccessful) {
							clearSignUp()
							saveLoginInfo(email, password)
							Router.navigate("auth/verify-email", false)
						} else {
							logcat("Error sending email:", sending.exception)
							onError(R.string.unkown_error)
						}
					}
				}
			}
		}
	}
}
