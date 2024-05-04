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

class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {

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

	fun logIn(onError: (Int) -> Unit) {
		val email = loginInfo.email
		val password = loginInfo.password
		auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { logIn ->
			val user = auth.currentUser
			// the user is null or anything went wrong
			if (user == null) {
				onError(R.string.unknown_error)
				logcat("Error sign in:", logIn.exception)
				return@addOnCompleteListener
			}

			// Invalid Credentials
			if (!logIn.isSuccessful) {
				onError(R.string.invalid_credentials)
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
					onError(R.string.unknown_error)
					logcat("Error read user document:", getUser.exception)
				} else {
					if (data["activated"] == true) {
						clearLogin()
						clearSignUp()
						Router.replace("home", true)
					} else {
						Router.navigate("auth/activation", false)
					}
				}
			}
		}
	}

	fun signUp(onError: (Int) -> Unit) {
		val email = signUpInfo.email
		val password = signUpInfo.password

		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { creating ->
			val user = auth.currentUser
			// Exit if the user is null or anything went wrong
			if (user == null || !creating.isSuccessful) {
				logcat("Error creating user:", creating.exception)
				onError(R.string.unknown_error)
				return@addOnCompleteListener
			}

			// create a new user collection and fill its content
			val userRef = Store.database.collection("user").document(user.uid)
			userRef.set(signUpInfo.toMap(user.uid)).addOnCompleteListener { saving ->
				if (!saving.isSuccessful) {
					logcat("Error creating user document: ", saving.exception)
					onError(R.string.unknown_error)
				} else {
					// Send a verification email to the user
					user.sendEmailVerification().addOnCompleteListener { sending ->
						if (sending.isSuccessful) {
							clearSignUp()
							saveLoginInfo(email, password)
							Router.navigate("auth/verification", false)
						} else {
							logcat("Error sending email:", sending.exception)
							onError(R.string.unknown_error)
						}
					}
				}
			}
		}
	}

	fun logOut(){
		Firebase.auth.signOut()
		Router.navigate("auth/log-in", false)
	}
}
