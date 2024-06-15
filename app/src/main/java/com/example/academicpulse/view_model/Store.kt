package com.example.academicpulse.view_model

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Store private constructor() : ViewModel() {
	// Is app ready
	private val isReady = MutableStateFlow(false)
	private var isLoading = MutableLiveData(false)

	// Store modules
	private val auth = AuthViewModel()
	private val user = UserViewModel()
	private val publications = PublicationsViewModel()
	private val publicationsTypes = PublicationsTypesViewModel()
	private val authors = PublicationsAuthorsViewModal()
	private val files = FilesViewModel()

	init {
		// The splash screen will disappear only when the logo animation finish and the user account is verified.
		val tasksCounter = MutableLiveData(1)
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
		// auth.signInOnStart(this.user, ::setIsReady)
	}

	// Note: Static variables and methods are used just to hold the global Store instance and be accessible in anywhere.
	companion object {
		// Note: An array type is used instead of Store to avoid null checks. We are certain that the app store will not be null as Provider method is one of the first functions called in the lifecycle.
		private val appStore = mutableListOf<Store>()

		/** Provider initialize the store instance that will be used across the entire App */
		@Composable
		fun Provider() {
			if (appStore.isEmpty()) appStore.add(Store())
		}

		// Static getters
		val isReady: Boolean
			get() = if (appStore.isEmpty()) false else appStore[0].isReady.value
		val isLoading: MutableLiveData<Boolean>
			get() = appStore[0].isLoading
		val auth: AuthViewModel
			get() = appStore[0].auth
		val user: UserViewModel
			get() = appStore[0].user
		val publications: PublicationsViewModel
			get() = appStore[0].publications
		val publicationsTypes: PublicationsTypesViewModel
			get() = appStore[0].publicationsTypes
		val authors: PublicationsAuthorsViewModal
			get() = appStore[0].authors
		val files: FilesViewModel
			get() = appStore[0].files
	}
}
