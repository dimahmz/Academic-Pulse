package com.example.academicpulse.view_model

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academicpulse.router.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class Store private constructor() : ViewModel() {
	// Is app ready
	private val isReady = MutableStateFlow(false)

	// Store modules
	private var auth = Auth()
	private var publications = Publications()
	private var publicationsTypes = PublicationsTypes()
	private var users = Users()
	private var files = Files()
	private var notifications = Notifications()
	private var applicationState = ApplicationState()

	init {
		onInit()
	}

	private fun onInit() {
		// The splash screen will disappear only when the logo animation finish and the user account is verified.
		val tasksCounter = MutableLiveData(2)
		fun setIsReady() {
			viewModelScope.launch {
				delay(600) // Wait for the navigation transition.
				tasksCounter.value = tasksCounter.value?.minus(1)
				if (tasksCounter.value == 0) {
					isReady.value = true
					Router.navigate("home")
					// get the user notifications
					Store.notifications.getUserNotifications({ }, { })
				}
			}
		}
		// A default splash screen animation delay.
		viewModelScope.launch {
			delay(1000)
			setIsReady()
		}
		auth.signInOnStart(this.users, ::setIsReady)
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
		val auth: Auth
			get() = appStore[0].auth
		val users: Users
			get() = appStore[0].users
		val publications: Publications
			get() = appStore[0].publications
		val publicationsTypes: PublicationsTypes
			get() = appStore[0].publicationsTypes
		val files: Files
			get() = appStore[0].files
		val notifications: Notifications
			get() = appStore[0].notifications
		val applicationState : ApplicationState
			get() = appStore[0].applicationState

		fun clear(resetLoading: Boolean = true) {
			if (appStore.isNotEmpty()) {
				if (resetLoading) appStore[0].isReady.value = false
				appStore[0].auth = Auth()
				appStore[0].publications = Publications()
				appStore[0].publicationsTypes = PublicationsTypes()
				appStore[0].users = Users()
				appStore[0].files = Files()
				appStore[0].notifications = Notifications()
				appStore[0].applicationState = ApplicationState()
			}
		}

		fun init() {
			if (appStore.isNotEmpty()) appStore[0].onInit()
		}
	}
}
