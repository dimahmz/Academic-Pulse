package com.example.academicpulse.view_model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import java.io.File

class Files : ViewModel() {
	// Firebase cloud storage instance
	private val storage = Firebase.storage.reference

	// Storage folders paths
	private val publications = "publications"

	private val listCache = arrayListOf<Pair<String, MutableLiveData<Uri>>>()
	private fun cacheFile(id: String, file: Uri?) {
		val index = listCache.indexOfFirst { (filename) -> id == filename }
		if (index == -1) listCache.add(Pair(id, MutableLiveData(file)))
		else listCache[index].second.value = file
	}

	fun readFile(id: String, onError: (Int) -> Unit, onSuccess: (Uri?) -> Unit) {
		val fullPath = "$publications/$id"
		val cachedFile = listCache.find { (id) -> fullPath == id }
		if (cachedFile != null) return onSuccess(cachedFile.second.value)
		val localFile = File.createTempFile("file1", ".pdf")
		storage.child(fullPath).getFile(localFile).addOnCompleteListener { uploading ->
			if (uploading.isSuccessful) {
				val file = Uri.fromFile(localFile)
				cacheFile(fullPath, file)
				onSuccess(file)
			} else {
				if (uploading.exception?.message?.contains("does not exist") == true) {
					logcat("Warning: File with ID name {$id} is not found in the path {$publications}")
					cacheFile(fullPath,  null)
					onSuccess(null)
				}
				else logcat("Error reading file with ID name {$id}", uploading.exception)
				onError(R.string.unknown_error)
			}
		}
	}

	fun uploadFile(file: Uri?, id: String, onError: (Int) -> Unit, onSuccess: () -> Unit) {
		if (file == null) {
			cacheFile("$publications/$id", null)
			return onSuccess()
		}
		storage.child("$publications/$id").putFile(file).addOnCompleteListener { uploading ->
			if (uploading.isSuccessful) {
				cacheFile("$publications/$id", file)
				onSuccess()
			} else {
				logcat("Error uploading new file with ID name {$id}", uploading.exception)
				onError(R.string.unknown_error)
			}
		}
	}
}
