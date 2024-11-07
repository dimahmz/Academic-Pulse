package com.example.academicpulse.view_model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.firebase.storage.storageMetadata
import java.io.File

class Files : ViewModel() {
	private val storage = Firebase.storage.reference
	private val publications = "publications"
	private val listCache = arrayListOf<Pair<String, MutableLiveData<Uri>>>()
	var metadata = storageMetadata {
		contentType = "application/pdf "
	}

	private fun cacheFile(name: String, file: Uri?) {
		val index = listCache.indexOfFirst { (filename) -> name == filename }
		if (index == -1) listCache.add(Pair(name, MutableLiveData(file)))
		else listCache[index].second.value = file
	}

	fun readFile(id: String, onError: (Int) -> Unit, onSuccess: (Uri?) -> Unit) {
		try {
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
						cacheFile(fullPath, null)
						onSuccess(null)
					} else logcat("Error reading file with ID name {$id}", uploading.exception)
					Store.applicationState.ShowServerErrorAlertDialog()
					onError(R.string.unknown_error)
				}
			}
		} catch (_: Exception) {
			onError(R.string.unknown_error)
		}
	}

	fun uploadFile(file: Uri?, id: String, onError: (Int) -> Unit, onSuccess: () -> Unit) {
		try {
			val fullPath = "$publications/$id"
			if (file == null) {
				cacheFile(fullPath, null)
				return onSuccess()
			}
			storage.child(fullPath).putFile(file, metadata).addOnCompleteListener { uploading ->
				if (uploading.isSuccessful) {
					cacheFile(fullPath, file)
					onSuccess()
				} else {
					logcat("Error uploading new file with ID name {$id}", uploading.exception)
					onError(R.string.unknown_error)
				}
			}
		} catch (exception: Exception) {
			logcat(exception = exception)
			onError(R.string.unknown_error)
		}
	}

	fun deleteFile(id: String, onFinish: () -> Unit) {
		try {
			val fullPath = "$publications/$id"
			listCache.removeIf { (filename) -> fullPath == filename }
			// TODO: Delete the file fullPath if exists
		} catch (exception: Exception) {
			logcat(exception = exception)
			onFinish()
		}
	}
}
