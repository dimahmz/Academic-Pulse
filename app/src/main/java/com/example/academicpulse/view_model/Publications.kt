package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.useCast
import com.google.firebase.firestore.Filter

class Publications : ViewModel() {
	private val collection = "publication"
	val userPublications = MutableLiveData<ArrayList<Publication>>()
	private val homePublications = MutableLiveData<ArrayList<Publication>>()
	val filteredHomePublications = MutableLiveData<ArrayList<Publication>>()
	val publication = MutableLiveData<Publication>()
	var selectedPublicationId = ""
	var redirectedFromForm = false
	var redirectedFromProfile = false
	val form = PublicationForm()

	fun search(
		query: String, onFinish: (ArrayList<Publication>) -> Unit
	) {
		if (homePublications.value == null) {
			onFinish(ArrayList())
			return
		}
		val searchQuery = query.trim().lowercase()
		onFinish(ArrayList(homePublications.value!!.filter {
			if (searchQuery.isBlank()) true
			else ("${it.title} ${it.abstract} ${it.type}").lowercase().contains(searchQuery)
		}))
	}

	fun belongsToThisUser(clickedPublication: Publication?): Boolean {
		if (clickedPublication == null) return false
		return clickedPublication.authors.find { it.id == Store.users.current.value!!.id } != null
	}

	fun fetchUserPublications(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		userPublications.value?.clear()
		Store.publicationsTypes.getAll(onError) {
			Store.users.getCurrentUser(onError) { user, _ ->
				StoreDB.getManyByIds<Publication>(collection,
					ids = useCast(user, "publications", arrayListOf()),
					onError = onError,
					onAsyncCast = { id, data, resolve ->
						Store.users.fetchAuthors(data) { list ->
							resolve(Publication.fromMap(id, data, list))
						}
					}) { list ->
					userPublications.value = list
					onSuccess()
				}
			}
		}
	}

	fun fetchHomePublication(onSuccess: () -> Unit, onError: (Int) -> Unit) {
		homePublications.value?.clear()
		val filter = arrayListOf(Filter.equalTo("status", "accepted"))
		Store.publicationsTypes.getAll(onError) {
			StoreDB.getMany<Publication>(
				collection,
				onError = onError,
				where = filter,
				onAsyncCast = { id, data, resolve ->
					Store.users.fetchAuthors(data) { list ->
						resolve(Publication.fromMap(id, data, list))
					}
				}) { list ->
				homePublications.value = list
				filteredHomePublications.value = list
				onSuccess()
			}
		}
	}

	fun fetchSelected(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		Store.publicationsTypes.getAll(onError) {
			val id = selectedPublicationId
			StoreDB.getOneById(collection, id, onError) { data, _ ->
				Store.users.fetchAuthors(data) { list ->
					publication.value = Publication.fromMap(id, data, list)
					onSuccess()
				}
			}
		}
	}

	fun insert(publication: Publication, onError: (error: Int) -> Unit) {
		// Get the current user ref because we need to update its publications list
		Store.users.getCurrentUser(onError) { user, userRef ->
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
					// Upload the publication file IF EXISTS
					Store.files.uploadFile(publication.file, id, onError) {
						selectedPublicationId = id
						redirectedFromForm = true
						form.form.clearAll()
						form.authors.value = arrayListOf()
						Router.navigate("publications/one-publication")
					}
				}
			}
		}
	}

	fun deleteById(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		val id = selectedPublicationId
		StoreDB.deleteOneById(collection, id, onError, onSuccess)
	}

	class PublicationForm {
		val form = Form.simple()
		val type = Field.simple(form = form, ifEmpty = R.string.type_required)
		val title = Field.simple(form = form, ifEmpty = R.string.title_required)
		val abstract = Field.simple(form = form, ifEmpty = R.string.abstract_required)
		val file = Field.simple(form = form, required = false)
		val doi = Field.simple(form = form, required = false)
		val date = Field.simple(form = form, ifEmpty = R.string.date_required)
		var authors = MutableLiveData(arrayListOf<User>())
	}
}
