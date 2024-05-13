package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.SignInInfo
import com.example.academicpulse.model.SignUpInfo
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {
	// Firebase cloud database instance
	private val db = Firebase.firestore
	private val auth = Firebase.auth

	val signInInfo: SignInInfo = SignInInfo()
	val signUpInfo: SignUpInfo = SignUpInfo()

	fun saveSignInInfo(email: String, password: String) {
		signInInfo.email = email
		signInInfo.password = password
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

	private fun clearSignIn() {
		signInInfo.email = ""
		signInInfo.password = ""
	}

	fun clearSignUp() {
		signUpInfo.institution = ""
		signUpInfo.department = ""
		signUpInfo.position = ""
		signUpInfo.firstName = ""
		signUpInfo.lastName = ""
		signUpInfo.email = ""
		signUpInfo.password = ""
	}

	fun signInOnStart(setIsReady: () -> Unit) {
		// If no user is logged in, keep the router in the sign up page, otherwise check for account activation.
		val user = auth.currentUser
		if (user == null) setIsReady()
		else
			db.collection("user").document(user.uid).get().addOnCompleteListener { userDoc ->
				val data = userDoc.result.data
				if (data != null && userDoc.isSuccessful) {
					if (data["activated"] == true) Router.replace("home", true)
					else if (data["activated"] == false) Router.replace("auth/activation", false)
				}
				setIsReady()
			}
	}

	fun signIn(onError: (Int) -> Unit) {
		val email = signInInfo.email
		val password = signInInfo.password
		auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signIn ->
			val user = auth.currentUser

			// Exit if the user is null or anything went wrong.
			if (signIn.exception != null) logcat(exception = signIn.exception)
			if (signIn.exception?.message?.contains("credential is incorrect") == true)
				return@addOnCompleteListener onError(R.string.invalid_credentials)
			if (user == null || !signIn.isSuccessful)
				return@addOnCompleteListener onError(R.string.unknown_error)
			if (!user.isEmailVerified)
				return@addOnCompleteListener onError(R.string.verify_email_first)

			// Check if the user account is activated
			val userRef = db.collection("user").document(user.uid)
			userRef.get().addOnCompleteListener { userDoc ->
				val data = userDoc.result.data
				if (data == null || !userDoc.isSuccessful) {
					onError(R.string.unknown_error)
					logcat("Error read user document:", userDoc.exception)
				} else if (data["activated"] == true) {
					clearSignIn()
					clearSignUp()
					Router.replace("home", true)
				} else Router.navigate("auth/activation", false)
			}
		}
	}

	fun signUp(onError: (Int) -> Unit) {
		val email = signUpInfo.email
		val password = signUpInfo.password

		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { creating ->
			val user = auth.currentUser
			// Exit if the user is null or anything went wrong
			if (user == null || !creating.isSuccessful)
				return@addOnCompleteListener onError(R.string.unknown_error)

			// create a new user collection and fill its content
			db.collection("user").document(user.uid).set(signUpInfo.toMap())
				.addOnCompleteListener { saving ->
					if (!saving.isSuccessful) onError(R.string.unknown_error)
					else {
						// Send a verification email to the user
						user.sendEmailVerification().addOnCompleteListener { sending ->
							if (sending.isSuccessful) {
								clearSignUp()
								saveSignInInfo(email, password)
								Router.navigate("auth/verification", false)
							} else onError(R.string.unknown_error)
						}
					}
				}
		}
	}

	fun logOut() {
		auth.signOut()
		Router.navigate("auth/sign-in", false)
	}
}
