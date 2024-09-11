package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.Notification
import com.example.academicpulse.model.Publication
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.forms.*
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.flow.MutableStateFlow

class Publications : ViewModel() {
	private val collection = "publication"
	private var listCache: ArrayList<Publication>? = null
	private var cacheInvalid = true
	private var cacheProfileInvalid = true
	var selectedPublicationId = ""
	val userPublications = MutableLiveData(arrayListOf<Publication>())
	val userFilteredPublications = MutableStateFlow(arrayListOf<Publication>())
	val isUserpublicationsFetched = MutableLiveData(false)
	var redirectedFromForm = false
	var redirectedFromProfile = false
	val form = PublicationForm()

	private fun cacheList(list: List<Publication>) {
		if (listCache == null) listCache = ArrayList(list)
		else list.forEach {
			val index = listCache!!.indexOfFirst { o -> o.id == it.id }
			if (index == -1) listCache!!.add(it)
			else listCache!![index] = it
		}
	}

	fun search(query: String, onFinish: (ArrayList<Publication>) -> Unit) {
		fun finish(fetchedPublications: ArrayList<Publication>) {
			val searchQuery = query.trim().lowercase()
			onFinish(
				ArrayList(fetchedPublications!!.filter {
					(searchQuery.isBlank() || it.title.lowercase()
						.contains(searchQuery)) && it.status == "accepted"
				})
			)
		}

		Store.publicationsTypes.getAll({ }) {
			StoreDB.getMany<Publication>(collection,
				where = listOf(Filter.equalTo("status", "accepted")),
				onAsyncCast = { id, data, resolve ->
					Store.users.fetchAuthors(data) { list ->
						resolve(Publication.fromMap(id, data, list))
					}
				},
				onError = { },
				onSuccess = { result ->
					finish(result)
				})
		}
	}

	fun fetchUserPublications(onFinish: (ArrayList<Publication>) -> Unit) {

		Store.publicationsTypes.getAll({ }) {
			Store.users.getCurrent({}) { user, _ ->
				StoreDB.getManyByIds<Publication>(collection,
					ids = user.publications,
					onAsyncCast = { id, data, resolve ->
						Store.users.fetchAuthors(data) { list ->
							resolve(Publication.fromMap(id, data, list))
						}
					},
					onError = { },
					onSuccess = { result ->
						isUserpublicationsFetched.value = true
						userPublications.value = result
						userFilteredPublications.value = result
						onFinish(result)
					})
			}
		}
	}

	fun fetchSelected(onSuccess: (Publication) -> Unit, onError: (error: Int) -> Unit) {
		Store.publicationsTypes.getAll(onError) {
			val id = selectedPublicationId
			val cachedPublication = listCache?.find { it.id == selectedPublicationId }
			if (cachedPublication != null) return@getAll onSuccess(cachedPublication)
			cacheInvalid = true
			cacheProfileInvalid = true
			StoreDB.getOneById(collection, id, onError) { data, _ ->
				Store.users.fetchAuthors(data) { list ->
					val publication = Publication.fromMap(id, data, list)
					cacheList(listOf(publication))
					onSuccess(publication)
				}
			}
		}
	}

	fun belongsToThisUser(publication: Publication?): Boolean {
		if (publication == null) return false
		return publication.authors.find { it.id == Store.users.current.value!!.id } != null
	}

	fun insert(publication: Publication, onError: (error: Int) -> Unit) {
		// Get the current user ref because we need to update its publications list
		Store.users.getCurrent(onError) { user, userRef ->
			// Insert the publication and get its generated id
			StoreDB.insert(collection, publication.toMap(), onError) { id ->
				publication.id = id
				// Insert the new publication id in the user publications then update it
				val publications = ArrayList(user.publications)
				publications.add(id)
				StoreDB.updateOneByRef(
					ref = userRef,
					data = hashMapOf("publications" to publications),
					onError = onError, // TODO : if this fails we should remove the publication
				) {
					// Upload the publication file IF EXISTS
					Store.files.uploadFile(publication.file, id, onError) {
						cacheList(listOf(publication))
						user.publications.add(publication.id)
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

	fun deleteSelected(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		val id = selectedPublicationId
		StoreDB.deleteOneById(collection, id, onError) {
			val publication = listCache!!.find { it.id == id }
			listCache!!.removeIf { p -> p.id == id }
			Store.users.current.value!!.publications.remove(id)
			publication!!.authors.forEach { author ->
				author.publications.remove(id)
				// TODO: Remove the publication id from the publications array of all authors collections
			}
			Store.files.deleteFile(id) {
				onSuccess()
			}
		}
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
