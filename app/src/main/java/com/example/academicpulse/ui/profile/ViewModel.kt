package com.example.academicpulse.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

	private val _text = MutableLiveData<String>().apply {
		value = "This is profile page"
	}
	val textContent: LiveData<String> = _text
}