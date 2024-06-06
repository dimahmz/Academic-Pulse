package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.useCast

class PublicationsViewModel : ViewModel() {
	private val collection = "publication"
	val userPublications = MutableLiveData<ArrayList<Publication>>()
	val homePublications = MutableLiveData<ArrayList<Publication>>()
	val publication = MutableLiveData<Publication>()
	var selectedPublicationId = ""
	var redirectedFromForm = false

	fun fetchUserPublications(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		userPublications.value?.clear()
		Store.publicationsTypes.getAll(onError) {
			Store.user.getCurrentUser(onError) { user, _ ->
				StoreDB.getManyByIds<Publication>(
					collection,
					ids = useCast(user, "publications", arrayListOf()),
					onError = onError,
					onAsyncCast = { id, data, resolve ->
						Store.authors.fetchPublicationAuthors(data) { list ->
							resolve(Publication.fromMap(id, data, list))
						}
					}
				) { list ->
					userPublications.value = list
					onSuccess()
				}
			}
		}
	}

	fun fetchHomePublication(onSuccess: () -> Unit, onError: (Int) -> Unit) {
		homePublications.value?.clear()
		Store.publicationsTypes.getAll(onError) {
			StoreDB.getMany<Publication>(
				collection,
				onError = onError,
				onAsyncCast = { id, data, resolve ->
					Store.authors.fetchPublicationAuthors(data) { list ->
						resolve(Publication.fromMap(id, data, list))
					}
				}
			) { list ->
				homePublications.value = list
				onSuccess()
			}
		}
	}

	fun fetchSelected(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		Store.publicationsTypes.getAll(onError) {
			val id = selectedPublicationId
			StoreDB.getOneById(collection, id, onError) { data, _ ->
				Store.authors.fetchPublicationAuthors(data) {
					publication.value = Publication.fromMap(id, data, it)
					onSuccess()
				}
			}
		}
	}

	fun insert(publication: Publication, onError: (error: Int) -> Unit) {
		// Get the current user ref because we need to update its publications list
		Store.user.getCurrentUser(onError) { user, userRef ->
			// Insert the publication and get its generated id
			StoreDB.insert(collection, publication.toMap(), onError) { id ->
				// Insert the new publication id in the user publications then update it
				val publications = useCast(user, "publications", arrayListOf<String>())
				publications.add(id)
				StoreDB.updateOneByRef(
					ref = userRef,
					data = hashMapOf("publications" to publications),
					onError = onError, // TODO : if this fails we should remove the publication
				) {
					selectedPublicationId = id
					redirectedFromForm = true
					Store.authors.currentForm.value = arrayListOf()
					Router.navigate("publications/one-publication", false)
				}
			}
		}
	}

	fun deleteById(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		val id = selectedPublicationId
		StoreDB.deleteOneById(collection, id, onError, onSuccess)
	}
}
