package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useCast

class PublicationsViewModel : ViewModel() {
	val userPublications = MutableLiveData<ArrayList<Publication>>()
	val publication = MutableLiveData<Publication>()
	var selectedPublicationId = ""

	fun fetchUserPublications(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		StoreDB.getCurrentUser(onError = onError) { user, _ ->
			StoreDB.getManyByIds(
				collection = "publication",
				ids = useCast(user, "publications", arrayListOf()),
				onCast = { id, data -> Publication.fromMap(id, data) },
				onError = onError,
			) { list, errors ->
				if (errors > 0) logcat("Getting user publications list was executed with $errors errors")
				userPublications.value = list
				onSuccess()
			}
		}
	}

	fun fetchSelected(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		val id = selectedPublicationId
		StoreDB.getOneById(collection = "publication", id = id, onError = onError) { data, _ ->
			publication.value = Publication.fromMap(id, data)
			onSuccess()
		}
	}

	fun insert(publication: Publication, onError: (error: Int) -> Unit) {
		// Get the current user ref because we need to update its publications list
		StoreDB.getCurrentUser(onError = onError) { user, userRef ->
			// Insert the publication and get its generated id
			StoreDB.insert(
				collection = "publication",
				data = publication.toMap(),
				onError = onError
			) { id ->
				// Insert the new publication id in the user publications then update it
				val publications = useCast(user, "publications", arrayListOf<String>())
				publications.add(id)
				StoreDB.updateOneByRef(
					ref = userRef,
					data = hashMapOf("publications" to publications),
					onError = onError, // TODO : if this fails we should remove the publication
				) {
					selectedPublicationId = id
					Router.navigate("publications/one-publication", false)
				}
			}
		}
	}
}
