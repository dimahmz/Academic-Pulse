package com.example.academicpulse.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.useCast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Filter

class Users : ViewModel() {
	private val collection = "user"
	private val auth = Firebase.auth
	private val realtimeDB = Firebase.database
	private var listCache: ArrayList<User>? = null
	private var cacheInvalid = true
	private val currentUser = MutableLiveData<User>()
	val current: LiveData<User> = currentUser

	private fun cacheList(list: ArrayList<User>) {
		if (listCache == null) listCache = ArrayList(list)
		else list.forEach {
			val index = listCache!!.indexOfFirst { o -> o.id == it.id }
			if (index == -1) listCache!!.add(it)
			else listCache!![index] = it
		}
	}

	fun getCurrent(
		onError: (error: Int) -> Unit, onSuccess: (data: User, ref: DocumentReference) -> Unit,
		onServerError: () -> Unit,
	) {
		if (currentUser.value != null) {
			return onSuccess(currentUser.value!!, StoreDB.refOf(collection, currentUser.value!!.id))
		}
		// the user isn't logged in
		val user = auth.currentUser

		if (user == null) {
			Router.navigate("auth/sign-in");
			return onError(R.string.unknown_error)
		}
		//
		StoreDB.getOneById(collection, user!!.uid, onError = {
			onServerError()
		}) { data, ref ->
			currentUser.value = User.fromMap(user.uid, data)
			onSuccess(currentUser.value!!, ref)
		}
	}

	fun getCurrentActivated(
		onError: (error: Int) -> Unit, onSuccess: (data: User, ref: DocumentReference) -> Unit
	) {
		getCurrent(onError = { onError(R.string.unknown_error) }, onServerError = {
			Store.applicationState.ShowServerErrorAlertDialog()
			onError(R.string.unknown_error)
		}, onSuccess = { user, ref ->
			run {
				val userRef = realtimeDB.getReference("users/${user.id}/activated")
				userRef.addValueEventListener(
					object : ValueEventListener {
						override fun onDataChange(dataSnapshot: DataSnapshot) {
							val isActivated: Boolean? = dataSnapshot.getValue<Boolean>()
							if (isActivated != null && isActivated) onSuccess(user, ref)
							else Router.navigate("auth/activation")
						}

						override fun onCancelled(error: DatabaseError) {
							if (!user.activated) Router.navigate("auth/activation")
							onSuccess(user, ref)
						}
					},
				)
			}.runCatching {
				onSuccess(user, ref)
			}
		})
	}

	fun search(
		query: String, selectedList: List<User>, onFinish: (ArrayList<User>) -> Unit
	) {
		val selected = ArrayList<String>(selectedList.map { it.id })
		selected.add(current.value!!.id)

		fun finish() {
			if (listCache == null) return onFinish(arrayListOf())
			val searchQuery = query.trim().lowercase()
			onFinish(
				ArrayList(listCache!!.filter {
					if (!it.activated || selected.contains(it.id)) false
					else if (searchQuery.isBlank()) true
					else if (("${it.firstName} ${it.lastName}").lowercase().contains(searchQuery)) true
					else ("${it.lastName} ${it.firstName}").lowercase().contains(searchQuery)
				})
			)
		}

		if (listCache != null && !cacheInvalid) return finish()
		StoreDB.getMany(
			collection,
			where = listOf(Filter.equalTo("activated", true)),
			onCast = { id, data -> User.fromMap(id, data) },
			onError = { finish() },
		) { result ->
			cacheInvalid = false
			cacheList(result)
			finish()
		}
	}

	fun fetchAuthors(
		publicationData: Map<String, Any?>,
		onFinish: (List<User>) -> Unit,
	) {
		val ids = useCast(publicationData, "authors", listOf<String>())
		fun finish() {
			if (listCache == null) return onFinish(arrayListOf())
			onFinish(listCache!!.filter { it.activated && ids.contains(it.id) })
		}

		if (listCache != null) return finish()
		cacheInvalid = true
		StoreDB.getManyByIds(
			collection,
			ids = ids,
			onCast = { id, data -> User.fromMap(id, data) },
			onError = { finish() },
		) { list ->
			cacheList(list)
			finish()
		}
	}
}
