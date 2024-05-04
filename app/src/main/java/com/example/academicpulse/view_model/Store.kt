package com.example.academicpulse.view_model

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Store : ViewModel() {
	private val isReady = MutableStateFlow(false)
	private val start = MutableLiveData("auth")

	private val auth = AuthViewModel(firebaseAuth)
	private val home = HomeViewModel()
	private val inbox = InboxViewModel()
	private val profile = ProfileViewModel()

	init {
		val store = this
		val tasksCounter = MutableLiveData(2)
		fun setIsReady() {
			viewModelScope.launch {
				delay(600)
				tasksCounter.value = tasksCounter.value?.minus(1)
				if (tasksCounter.value == 0) store.isReady.value = true
			}
		}

		viewModelScope.launch {
			delay(1000)
			setIsReady()
		}

		// If no user is logged in, keep the router in sign up page, otherwise check for account activation
		val user = firebaseAuth.currentUser
		if (user == null) setIsReady()
		else
			database.collection("user").document(user.uid).get().addOnCompleteListener { doc ->
				val data = doc.result.data
				if (doc.isSuccessful && data!!["activated"] == true) start.value = "home"
				else if (doc.isSuccessful && data!!["activated"] == false) start.value = "authActivation"
				setIsReady()
			}
	}

	// Note: Static variables and methods are used just to hold the global Store instance and be accessible in anywhere.
	companion object {
		// Note: An array type is used instead of Store to avoid null checks. We are certain that the app store will not be null as Provider method is one of the first functions called in the lifecycle.
		private val appStore = mutableListOf<Store>()

		// Firebase cloud database and authentication instances
		val database by lazy { Firebase.firestore }
		private val firebaseAuth: FirebaseAuth = Firebase.auth

		/** Provider initialize the store instance that will be used across the entire App */
		@Composable
		fun Provider() {
			if (appStore.isEmpty()) appStore.add(Store())
		}

		fun isReady(): Boolean {
			return if (appStore.isEmpty()) false else appStore[0].isReady.value
		}

		fun getStartDestination(): LiveData<String> {
			return appStore[0].start
		}

		fun auth(): AuthViewModel {
			return appStore[0].auth
		}

		fun home(): HomeViewModel {
			return appStore[0].home
		}

		fun inbox(): InboxViewModel {
			return appStore[0].inbox
		}

		fun profile(): ProfileViewModel {
			return appStore[0].profile
		}
	}
}
