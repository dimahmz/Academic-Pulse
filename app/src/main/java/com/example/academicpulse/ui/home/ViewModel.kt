package com.example.academicpulse.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ViewModel : ViewModel() {
	private val db = Firebase.firestore

	private val _counter = MutableLiveData<Int>().apply {
		value = 0
	}
	fun increase() {
		val value = _counter.value?.plus(1)?: 0
		val message = hashMapOf<String, Any>(
			"value" to value
		)
		db.collection("message").document().set(message)
			.addOnSuccessListener {
				_counter.value = value
			}
	}
	val counter: LiveData<Int> = _counter
}