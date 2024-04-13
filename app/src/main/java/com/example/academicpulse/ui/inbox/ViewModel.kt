package com.example.academicpulse.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

	private val _text = MutableLiveData<String>().apply {
		value = "This is inbox page"
	}
	val text: LiveData<String> = _text
}