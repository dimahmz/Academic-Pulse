package com.example.academicpulse.view_model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academicpulse.model.LoginInfo
import com.example.academicpulse.model.SignUpInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    //Firebase Authentication
    private val auth: FirebaseAuth = Firebase.auth

    // Could Firestore database
    private val db = Firebase.firestore


    val loginInfo: LoginInfo = LoginInfo()
    val signUpInfo: SignUpInfo = SignUpInfo()

    fun saveLoginInfo(email: String, password: String) {
        loginInfo.email = email
        loginInfo.password = password
    }

    fun saveInstitutionInfo(
        institution: String,
        department: String,
        position: String,
        skipped: Boolean
    ) {
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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess(true)
                    } else {
                        onSuccess(false)
                    }
                }
        }
    }

    fun signup(onSuccess: (Boolean, String) -> Unit) {
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
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // get the current user
                        val user = auth.currentUser
                        // Exit if user is null
                        val uid = user?.uid ?: return@addOnCompleteListener
                        // create a new user collection
                        val userRef = db.collection("users").document(uid)
                        // collection fields
                        val userData = hashMapOf(
                            "uid" to uid,
                            "email" to email,
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "institution" to institution,
                            "department" to department,
                            "position" to position,
                            "role" to "user"
                        )
                        // add user data to the users collection
                        userRef.set(userData)
                            .addOnSuccessListener {
                                // send an email verification to the user
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // user has been created
                                            onSuccess(true, "Account has been created")
                                            Log.d(TAG, "Email sent.")
                                        } else {
                                            onSuccess(
                                                false,
                                                task.exception?.message
                                                    ?: "An unknown error occurred"
                                            )
                                        }
                                    }
                            }
                            .addOnFailureListener { exception ->
                                Log.e(TAG, "Error creating user document: ", exception)
                                onSuccess(false, exception.message ?: "An unknown error occurred")
                            }
                    } else {
                        onSuccess(false, task.exception?.message ?: "An unknown error occurred")
                        Log.e(TAG, "Error creating user:", task.exception)
                    }
                }
        }
    }
}
