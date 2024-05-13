package com.example.academicpulse.view_model

import androidx.compose.runtime.mutableIntStateOf
import com.example.academicpulse.R
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

class StoreDB private constructor() {
	companion object {
		private val db by lazy { Firebase.firestore }
		private val auth = Firebase.auth

		fun getCurrentUser(
			onError: (error: Int) -> Unit,
			onSuccess: (data: Map<String, Any?>, ref: DocumentReference) -> Unit
		) {
			val user = auth.currentUser ?: return onError(R.string.unknown_error)
			getOneById(collection = "user", id = user.uid, onError = onError) { data, ref ->
				onSuccess(data, ref)
			}
		}

		fun getOneById(
			collection: String,
			id: String,
			onError: (error: Int) -> Unit,
			onSuccess: (data: Map<String, Any?>, ref: DocumentReference) -> Unit,
		) {
			val ref = db.collection(collection).document(id)
			ref.get().addOnCompleteListener { doc ->
				val data = doc.result.data
				if (doc.isSuccessful && data != null) onSuccess(data, ref)
				else if (data == null) {
					logcat("Warning: Document with id {$id} is not found in the collection {$collection}")
					onError(R.string.unknown_error)
				} else {
					val message = "Error getting document by id {$id} from the collection {$collection}"
					logcat(message, doc.exception)
					onError(R.string.unknown_error)
				}
			}
		}

		fun <T> getManyByIds(
			collection: String,
			ids: ArrayList<String>,
			onCast: (id: String, map: Map<String, Any?>) -> T,
			onError: (error: Int) -> Unit,
			onSuccess: (list: ArrayList<T>, errors: Int) -> Unit
		) {
			if (ids.size == 0) return onSuccess(arrayListOf(), 0)
			val list = arrayListOf<T>()
			val size = mutableIntStateOf(ids.size)
			val errors = mutableIntStateOf(0)
			ids.forEach { id ->
				db.collection(collection).document(id).get().addOnCompleteListener { doc ->
					val data = doc.result.data
					if (doc.isSuccessful && data != null) list.add(onCast(id, data))
					else if (data == null) {
						logcat("Warning: Document with id {$id} is not found in the collection {$collection}")
					} else {
						val message = "Error getting document with id {$id} from the collection {$collection}"
						errors.intValue = errors.intValue.plus(1)
						logcat(message, doc.exception)
						onError(R.string.unknown_error)
					}
					size.intValue--
					if (size.intValue == 0) {
						if (errors.intValue != ids.size) onSuccess(list, errors.intValue)
						else onError(R.string.unknown_error)
					}
				}
			}
		}

		fun insert(
			collection: String,
			data: HashMap<String, Any?>,
			onError: (error: Int) -> Unit,
			onSuccess: (id: String) -> Unit,
		) {
			db.collection(collection).add(data).addOnCompleteListener { inserting ->
				if (inserting.isSuccessful) onSuccess(inserting.result.id)
				else {
					val message = "Error inserting new document in the collection {$collection}"
					logcat(message, inserting.exception)
					onError(R.string.unknown_error)
				}
			}
		}

		fun updateOneByRef(
			ref: DocumentReference,
			data: HashMap<String, Any?>,
			onError: (error: Int) -> Unit,
			onSuccess: () -> Unit,
		) {
			ref.set(data, SetOptions.merge()).addOnCompleteListener { updating ->
				if (updating.isSuccessful) onSuccess()
				else {
					logcat("Error updating document by ref", updating.exception)
					onError(R.string.unknown_error)
				}
			}
		}
	}
}
