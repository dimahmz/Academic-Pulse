package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class ApplicationState : ViewModel() {
	val hasServerErrorOccurred = MutableStateFlow(false)

	fun ShowServerErrorAlertDialog() {
		hasServerErrorOccurred.value = true
	}

	fun hideAlterMessage() {
		hasServerErrorOccurred.value = false
	}
}



