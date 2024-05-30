package com.example.academicpulse.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference

class UserViewModel : ViewModel() {
	private val collection = "user"
	private val auth = Firebase.auth
	private val currentUser = MutableLiveData<User>()

	val current: LiveData<User> = currentUser

	fun getCurrentUser(
		onError: (error: Int) -> Unit,
		onSuccess: (data: Map<String, Any?>, ref: DocumentReference) -> Unit
	) {
		val user = auth.currentUser ?: return onError(R.string.unknown_error)
		StoreDB.getOneById(collection, user.uid, onError) { data, ref ->
			currentUser.value = User.fromMap(user.uid, data)
			onSuccess(data, ref)
		}
	}
}
