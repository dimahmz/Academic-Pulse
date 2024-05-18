package com.example.academicpulse.view_model

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.PublicationType
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.useCast
import java.util.concurrent.CountDownLatch

class PublicationsViewModel : ViewModel() {
	val userPublications = MutableLiveData<ArrayList<Publication>>(arrayListOf())
	val publication = MutableLiveData<Publication>()
	var selectedPublicationId = ""
	var publicationTypes = MutableLiveData<ArrayList<PublicationType>>(arrayListOf())

	fun fetchUserPublications(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		Store.user.getCurrentUser(onError = onError) { user, _ ->
			val ids = useCast(user, "publications", arrayListOf<String>())
			val numPublications = ids.size
			val listOfUserPublications = arrayListOf<Publication>()
			val countDownLatch = CountDownLatch(numPublications)
			if (numPublications == 0) {
				onSuccess()
				return@getCurrentUser
			}
			ids.forEach {
				// get one publication
				StoreDB.getOneById(collection = "publication", id = it, onError = {
					countDownLatch.countDown()
				}) { data, _ ->
					if (data != null) {
						var oneUserPublication = Publication.fromMap(it, data)
						// fetch the type of this publication
						StoreDB.getOneById(collection = "publicationType",
							id = oneUserPublication.typeId,
							onError = {
								countDownLatch.countDown()
								if (countDownLatch.count == 0L) {
									userPublications.value = listOfUserPublications
									onSuccess()
								}
							}) { data, _ ->
							if (data != null) {
								val publicationType = PublicationType.fromMap(oneUserPublication.typeId, data)
								oneUserPublication.type = publicationType.label
								listOfUserPublications.add(oneUserPublication)
								countDownLatch.countDown()
								if (countDownLatch.count == 0L) {
									userPublications.value = listOfUserPublications
									onSuccess()
								}
							} else {
								countDownLatch.countDown()
								if (countDownLatch.count == 0L) {
									userPublications.value = listOfUserPublications
									onSuccess()
								}
							}
						}
					}
				}
			}
		}
	}

	fun fetchPublicationTypes(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		StoreDB.getAll("publicationType", onError = onError) { result ->
			val _publicationTypes = ArrayList<PublicationType>(arrayListOf())
			for (document in result) {
				_publicationTypes.add(PublicationType.fromMap(document.id, document.data))
			}
			publicationTypes.value = _publicationTypes
			onSuccess()
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
		Store.user.getCurrentUser(onError = onError) { user, userRef ->
			// Insert the publication and get its generated id
			StoreDB.insert(
				collection = "publication", data = publication.toMap(), onError = onError
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