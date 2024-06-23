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
	private var listCache = arrayListOf<Publication>()
	var selectedPublicationId = ""
	var redirectedFromForm = false
	var redirectedFromProfile = false
	val form = PublicationForm()

	private fun cacheList(list: List<Publication>) {
		list.forEach {
			val index = listCache.indexOfFirst { author -> author.id == it.id }
			if (index == -1) listCache.add(it)
			else listCache[index] = it
		}
	}

	fun search(query: String, onFinish: (ArrayList<Publication>) -> Unit) {
		fun finish() {
			val searchQuery = query.trim().lowercase()
			onFinish(
				if (searchQuery.isBlank()) listCache
				else ArrayList(listCache.filter { it.title.lowercase().contains(searchQuery) })
			)
		}

		Store.publicationsTypes.getAll({ finish() }) {
			StoreDB.getMany<Publication>(
				collection,
				where = listOf(Filter.equalTo("status", "accepted")),
				onAsyncCast = { id, data, resolve ->
					Store.users.fetchAuthors(data) { list ->
						resolve(Publication.fromMap(id, data, list))
					}
				},
				onError = { finish() },
				onSuccess = { result ->
					cacheList(result)
					finish()
				}
			)
		}
	}

	fun fetchUserPublications(onFinish: (ArrayList<Publication>) -> Unit) {
		fun finish() {
			onFinish(ArrayList(listCache.filter { belongsToThisUser(it) }))
		}

		Store.publicationsTypes.getAll({ finish() }) {
			Store.users.getCurrentUser({ finish() }) { user, _ ->
				StoreDB.getManyByIds<Publication>(
					collection,
					ids = useCast(user, "publications", arrayListOf()),
					onAsyncCast = { id, data, resolve ->
						Store.users.fetchAuthors(data) { list ->
							resolve(Publication.fromMap(id, data, list))
						}
					},
					onError = { finish() },
					onSuccess = { result ->
						cacheList(result)
						finish()
					}
				)
			}
		}
	}

	fun fetchSelected(onSuccess: (Publication) -> Unit, onError: (error: Int) -> Unit) {
		Store.publicationsTypes.getAll(onError) {
			val id = selectedPublicationId
			val cachedPub = listCache.find { it.id == selectedPublicationId }
			if (cachedPub != null) onSuccess(cachedPub)
			StoreDB.getOneById(collection, id, onError) { data, _ ->
				Store.users.fetchAuthors(data) { list ->
					val pub = Publication.fromMap(id, data, list)
					cacheList(listOf(pub))
					onSuccess(pub)
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
