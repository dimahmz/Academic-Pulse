package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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
			delay(1600L)
			isReady.value = true
		}
	}

	// Note: Static variables and methods are used just to hold the global Store instance and be accessible in anywhere.
	companion object {
		private val appStore = Store()

		fun isReady(): Boolean {
			return appStore.isReady.value
		}

		fun auth(): AuthViewModel {
			return appStore.auth
		}

		fun home(): HomeViewModel {
			return appStore.home
		}

		fun inbox(): InboxViewModel {
			return appStore.inbox
		}

		fun profile(): ProfileViewModel {
			return appStore.profile
		}
	}
}
