package com.example.academicpulse.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.PublicationType

class PublicationsTypes : ViewModel() {
	private val collection = "publicationType"
	private val _publicationTypes = MutableLiveData<ArrayList<PublicationType>>()
	private var alreadyFetched = false

	val list: LiveData<ArrayList<PublicationType>> = _publicationTypes

	fun getAll(onError: (error: Int) -> Unit, onSuccess: (ArrayList<PublicationType>) -> Unit) {
		if (alreadyFetched) return onSuccess(_publicationTypes.value!!)
		StoreDB.getMany(
			collection,
			onError = onError,
			onCast = { id, data -> PublicationType.fromMap(id, data) }
		) { list ->
			_publicationTypes.value = list
			alreadyFetched = true
			onSuccess(_publicationTypes.value!!)
		}
	}
}
