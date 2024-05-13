package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useCast

class PublicationsViewModel : ViewModel() {
	val userPublications = MutableLiveData(arrayListOf<Publication>())
	val selectedPublication = MutableLiveData(Publication())

	fun fetchUserPublications(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		StoreDB.getCurrentUser(onError = onError) { userDoc ->
			StoreDB.getManyByIds(
				collection = "publication",
				ids = useCast(userDoc.result, "publications", arrayListOf()),
				onCast = { Publication.fromMap(it) },
				onError = onError,
			) { list, errors ->
				if (errors > 0) logcat("Getting user publications list was executed with $errors errors")
				userPublications.value = list
				onSuccess()
			}
		}
	}

	fun fetchOneById(id: String, onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		StoreDB.getOneById(
			collection = "publication",
			id = id,
			onError = onError,
		) { data ->
			selectedPublication.value = Publication.fromMap(data)
			logcat(selectedPublication.value.toString())
			onSuccess()
		}
	}

	fun insert(publication: Publication, onError: (error: Int) -> Unit) {
		// Get the current user ref because we need to update its publications list
		StoreDB.getCurrentUser(onError = onError) { userDoc, userRef ->
			// Insert the publication and get its generated id
			StoreDB.insert(
				collection = "publication",
				data = publication.toMap(),
				onError = onError
			) { id ->
				// Insert the new publication id in the user publications then update it
				val publications = useCast(userDoc.result, "publications", arrayListOf<String>())
				publications.add(id)
				StoreDB.updateOneByRef(
					ref = userRef,
					data = hashMapOf("publications" to publications),
					onError = onError, // TODO : if this fails we should remove the publication
				) { Router.back() }
			}
		}
	}
}
