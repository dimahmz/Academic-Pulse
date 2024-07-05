package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.SignInInfo
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class Auth : ViewModel() {
	private val db = Firebase.firestore
	private val auth = Firebase.auth
	private val realtimeDB = Firebase.database
	var signInInfo: SignInInfo = SignInInfo("", "")
	val signUpInfo: User = User()

	fun saveInstitutionInfo(
		institution: String, department: String, position: String
	) {
		signUpInfo.institution = institution
		signUpInfo.department = department
		signUpInfo.position = position
	}

	fun saveSignUpInfo(firstName: String, lastName: String, email: String) {
		signUpInfo.firstName = firstName
		signUpInfo.lastName = lastName
		signUpInfo.email = email
	}

	fun clearSignUp() {
		signUpInfo.institution = ""
		signUpInfo.department = ""
		signUpInfo.position = ""
		signUpInfo.firstName = ""
		signUpInfo.lastName = ""
		signUpInfo.email = ""
	}

	fun signInOnStart(vm: Users, setIsReady: () -> Unit) {
		// If no user is logged in, keep the router in the sign up page, otherwise check for account activation.
		vm.getCurrent(onError = { setIsReady() }) { user, _ ->
			val userRef = realtimeDB.getReference("users/${user.id}/activated")
			userRef.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					val isActivated: Boolean? = dataSnapshot.getValue<Boolean>()
					if (isActivated != null && isActivated) Router.navigate("home")
					else Router.navigate("auth/activation")
					setIsReady()
				}
				override fun onCancelled(error: DatabaseError) {
					if (user.activated) Router.navigate("home")
					else Router.navigate("auth/activation")
					setIsReady()
				}
			})
		}
	}

	fun signIn(info: SignInInfo, onError: (Int) -> Unit) {
		try {
			signInInfo = info
			auth.signInWithEmailAndPassword(info.email, info.password).addOnCompleteListener { signIn ->
				if (signIn.exception != null) logcat(exception = signIn.exception)
				if (signIn.exception?.message?.contains("credential is incorrect") == true) return@addOnCompleteListener onError(
					R.string.invalid_credentials
				)
				if (!signIn.isSuccessful) return@addOnCompleteListener onError(R.string.unknown_error)
				val currentUser = auth.currentUser
				if (currentUser != null && !currentUser.isEmailVerified) return@addOnCompleteListener onError(
					R.string.verify_email_first
				)

				// Check the user account activation.
				Store.users.getCurrent(onError = onError) { user, _ ->
					if (user.activated) {
						signInInfo = SignInInfo("", "")
						clearSignUp()
						Router.navigate("home")
					} else Router.navigate("auth/activation")
				}
			}
		} catch (exception: Exception) {
			logcat(exception = exception)
			onError(R.string.unknown_error)
		}
	}

	fun signUp(info: SignInInfo, onError: (Int) -> Unit) {
		try {
			auth.createUserWithEmailAndPassword(info.email, info.password)
				.addOnCompleteListener { creating ->
					val user = auth.currentUser
					// Exit if the user is null or anything went wrong
					if (user == null || !creating.isSuccessful) return@addOnCompleteListener onError(R.string.unknown_error)

					// create a new user collection and fill its content
					db.collection("user").document(user.uid).set(signUpInfo.toMap())
						.addOnCompleteListener { saving ->
							if (!saving.isSuccessful) onError(R.string.unknown_error)
							else {
								// add a the new user the realtime database
								val newUserRef: DatabaseReference =
									realtimeDB.reference.child("users").child("${user.uid}");
								newUserRef.child("activated").setValue(false).addOnSuccessListener {
									// Send a verification email to the user
									user.sendEmailVerification().addOnCompleteListener { sending ->
										if (sending.isSuccessful) {
											clearSignUp()
											signInInfo = info
											Router.navigate("auth/verification")
											// user is logged in!
											auth.signOut()
										} else onError(R.string.unknown_error)
									}.addOnFailureListener {
										onError(R.string.unknown_error)
									}
								};
							}
						}
				}
		} catch (exception: Exception) {
			logcat(exception = exception)
			onError(R.string.unknown_error)
		}
	}

	fun logOut() {
		try {
			auth.signOut()
			Store.clear(false)
			Router.navigate("auth/sign-in")
		} catch (exception: Exception) {
			logcat(exception = exception)
		}
	}
}