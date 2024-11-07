package com.example.academicpulse.view_model

import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.PublicationType

class PublicationsTypes : ViewModel() {
	private val collection = "publicationType"
	private var listCache: List<PublicationType>? = null

	fun getOne(id: String): PublicationType? {
		return listCache?.find { it.id == id }
	}

	fun getAll(onError: (error: Int) -> Unit, onSuccess: (List<PublicationType>) -> Unit) {
		if (listCache != null) return onSuccess(listCache!!)
		StoreDB.getMany(collection, onError = {
			onError(it)
		}, onCast = { id, data -> PublicationType.fromMap(id, data) }) { list ->
			listCache = list
			onSuccess(list)
		}
	}
}
