package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import com.example.academicpulse.model.User
import com.example.academicpulse.utils.useCast
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter

class PublicationsAuthorsViewModal {
	var currentForm = MutableLiveData(arrayListOf<User>())
	private var listCache = arrayListOf<User>()

	private fun cacheList(list: ArrayList<User>) {
		list.forEach {
			val index = listCache.indexOfFirst { author -> author.id == it.id }
			if (index == -1) listCache.add(it)
			else listCache[index] = it
		}
	}

	fun search(
		query: String,
		selectedList: List<User>,
		onFinish: (ArrayList<User>) -> Unit
	) {
		val selected = ArrayList<String>(selectedList.map { it.id })
		selected.add(Store.user.current.value!!.id)

		fun finish() {
			val searchQuery = query.trim().lowercase()
			onFinish(
				ArrayList(listCache.filter {
					if (selected.contains(it.id)) false
					else if (searchQuery == "") true
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

	fun fetchPublicationAuthors(
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
