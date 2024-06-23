package com.example.academicpulse.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.User
import com.example.academicpulse.utils.useCast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter

class Users : ViewModel() {
	private val collection = "user"
	private val auth = Firebase.auth
	private val currentUser = MutableLiveData<User>()
	val current: LiveData<User> = currentUser
	private var listCache = arrayListOf<User>()

	private fun cacheList(list: ArrayList<User>) {
		list.forEach {
			val index = listCache.indexOfFirst { author -> author.id == it.id }
			if (index == -1) listCache.add(it)
			else listCache[index] = it
		}
	}

	fun getCurrentUser(
		onError: (error: Int) -> Unit,
		onSuccess: (data: Map<String, Any?>, ref: DocumentReference) -> Unit
	) {
		val user = auth.currentUser ?: return onError(R.string.unknown_error)
		StoreDB.getOneById(collection, user.uid, onError) { data, ref ->
			currentUser.value = User.fromMap(user.uid, data)
			onSuccess(data, ref)
		}
	}

	fun search(
		query: String,
		selectedList: List<User>,
		onFinish: (ArrayList<User>) -> Unit
	) {
		val selected = ArrayList<String>(selectedList.map { it.id })
		selected.add(current.value!!.id)

		fun finish() {
			val searchQuery = query.trim().lowercase()
			onFinish(
				ArrayList(listCache.filter {
					if (selected.contains(it.id)) false
					else if (searchQuery.isBlank()) true
					else if (("${it.firstName} ${it.lastName}").lowercase().contains(searchQuery)) true
					else ("${it.lastName} ${it.firstName}").lowercase().contains(searchQuery)
				})
			)
		}

		StoreDB.getMany(
			collection = "user",
			where = listOf(Filter.notInArray(FieldPath.documentId(), selected)),
			onCast = { id, data -> User.fromMap(id, data) },
			onError = { finish() },
		) { result ->
			cacheList(result)
			finish()
		}
	}

	fun fetchAuthors(
		publicationData: Map<String, Any?>,
		onSuccess: (List<User>) -> Unit,
	) {
		val ids = useCast(publicationData, "authors", listOf<String>())
		StoreDB.getManyByIds(
			collection = "user",
			ids = ids,
			onCast = { id, data -> User.fromMap(id, data) },
			onError = { onSuccess(listCache.filter { ids.contains(it.id) }) },
		) { list ->
			cacheList(list)
			onSuccess(list)
		}
	}
}
