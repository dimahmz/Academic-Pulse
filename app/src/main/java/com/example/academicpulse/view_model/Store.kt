package com.example.academicpulse.view_model

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Store private constructor(): ViewModel() {
	private val isReady = MutableStateFlow(false)
	private val auth = AuthViewModel()
	private val home = HomeViewModel()
	private val inbox = InboxViewModel()
	private val profile = ProfileViewModel()
	private val publication = PublicationViewModel()

	init {
		// The splash screen will disappear only when the logo animation finish and the user account is verified.
		val tasksCounter = MutableLiveData(2)
		fun setIsReady() {
			viewModelScope.launch {
				delay(600) // Wait for the navigation transition.
				tasksCounter.value = tasksCounter.value?.minus(1)
				if (tasksCounter.value == 0) isReady.value = true
			}
		}
		// A default splash screen animation delay.
		viewModelScope.launch {
			delay(1000)
			setIsReady()
		}
		auth.signInOnStart(::setIsReady)
	}

	// Note: Static variables and methods are used just to hold the global Store instance and be accessible in anywhere.
	companion object {
		// Note: An array type is used instead of Store to avoid null checks. We are certain that the app store will not be null as Provider method is one of the first functions called in the lifecycle.
		private val appStore = mutableListOf<Store>()

		// Firebase cloud database instance
		val database by lazy { Firebase.firestore }

		/** Provider initialize the store instance that will be used across the entire App */
		@Composable
		fun Provider() {
			if (appStore.isEmpty()) appStore.add(Store())
		}

		// Static getters
		val isReady: Boolean
			get() = if (appStore.isEmpty()) false else appStore[0].isReady.value
		val auth: AuthViewModel
			get() = appStore[0].auth
		val home: HomeViewModel
			get() = appStore[0].home
		val inbox: InboxViewModel
			get() = appStore[0].inbox
		val profile: ProfileViewModel
			get() = appStore[0].profile
		val publication: PublicationViewModel
			get() = appStore[0].publication
	}
}
