package com.example.academicpulse.view_model

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Store : ViewModel() {
	private val auth = AuthViewModel()
	private val home = HomeViewModel()
	private val inbox = InboxViewModel()
	private val profile = ProfileViewModel()
	private val isReady = MutableStateFlow(false)

	init {
		viewModelScope.launch {
			val user = Firebase.auth.currentUser
			val isLoggedIn = user != null

			// no user is logged in
			if (!isLoggedIn) {
				Router.navigate("auth/log-in", false)
				isReady.value = true
				return@launch
			}

			// fetch the user document
			val userRef =  Firebase.firestore.collection("user").document(user!!.uid)
			userRef.get().addOnCompleteListener { getUser ->
				val data = getUser.result.data
				// user without a document or unexpected error
				if (data == null || !getUser.isSuccessful) {
					Router.navigate("auth/log-in", false)
					logcat("Error read user document:", getUser.exception)
				} else {
					if (data["activated"] == true) {
						Router.navigate("profile", true)
					} else {
						Router.navigate("auth/activation", false)
					}
				}
			}
			isReady.value = true
		}
	}

	// Note: Static variables and methods are used just to hold the global Store instance and be accessible in anywhere.
	companion object {
		// Note: An array type is used instead of Store to avoid null checks. We are certain that the app store will not be null as Provider method is one of the first functions called in the lifecycle.
		private val appStore = mutableListOf<Store>()

		// Firebase database cloud instance
		val database by lazy { Firebase.firestore }

		/** Provider initialize the store instance that will be used across the entire App */
		@Composable
		fun Provider() {
			if (appStore.isEmpty()) appStore.add(Store())
		}

		fun isReady(): Boolean {
			return appStore[0].isReady.value
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
