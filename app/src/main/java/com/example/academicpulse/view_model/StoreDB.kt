package com.example.academicpulse.view_model

import androidx.compose.runtime.mutableIntStateOf
import com.example.academicpulse.R
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class StoreDB private constructor() {
	companion object {
		private val db by lazy { Firebase.firestore }

		fun refOf(collection: String, id: String): DocumentReference {
			return db.collection(collection).document(id)
		}

		fun getOneById(
			collection: String,
			id: String,
			onError: (error: Int) -> Unit,
			onSuccess: (data: Map<String, Any?>, ref: DocumentReference) -> Unit,
		) {
			try {
				val ref = refOf(collection, id)
				ref.get().addOnCompleteListener { task ->
					if (task.isSuccessful) {
						val data = task.result.data
						if (data != null) {
							onSuccess(data, ref)
						} else {
							// Document not found
							val message = "Document with id $id not found in collection $collection"
							logcat(message)
							onError(R.string.document_not_found) // Assuming you have a specific error for not found
						}
					} else {
						// Error while retrieving document
						val message = "Error getting document by id $id from collection $collection"
						logcat(message, task.exception)
						Store.applicationState.ShowServerErrorAlertDialog()
						onError(R.string.unknown_error)
					}
				}
			} catch (exception: Exception) {
				logcat(exception = exception)
				Store.applicationState.ShowServerErrorAlertDialog()
				onError(R.string.unknown_error)
			}
		}

		fun <T> getManyByIds(
			collection: String,
			ids: List<String>,
			onError: (error: Int) -> Unit,
			onCast: ((id: String, map: Map<String, Any?>) -> T)? = null,
			onAsyncCast: ((id: String, map: Map<String, Any?>, resolver: (T) -> Unit) -> Unit)? = null,
			onSuccess: (list: ArrayList<T>) -> Unit
		) {
			try {
				if (ids.isEmpty()) return onSuccess(arrayListOf())
				val list = arrayListOf<T>()
				val size = mutableIntStateOf(ids.size)
				val errors = mutableIntStateOf(0)

				fun countDown() {
					size.intValue--
					if (size.intValue <= 0) {
						if (errors.intValue != ids.size) onSuccess(list)
						else onError(R.string.unknown_error)
					}
				}

				ids.forEach { id ->
					refOf(collection, id).get().addOnCompleteListener { doc ->
						if (doc.isSuccessful && doc.result.data != null) {
							castDocument(doc.result, onCast, onAsyncCast, ::countDown) { list.add(it) }
						} else {
							if (doc.result.data == null) {
								logcat("Warning: Document with id {$id} is not found in the collection {$collection}")
							} else {
								val message =
									"Error getting document with id {$id} from the collection {$collection}"
								errors.intValue = errors.intValue.plus(1)
								logcat(message, doc.exception)
								onError(R.string.unknown_error)
							}
							countDown()
						}
					}
				}
			} catch (exception: Exception) {
				logcat(exception = exception)
				onError(R.string.unknown_error)
			}
		}

		fun <T> getMany(
			collection: String,
			where: List<Filter>? = null,
			limit: Int? = null,
			onError: (error: Int) -> Unit,
			onCast: ((id: String, map: Map<String, Any?>) -> T)? = null,
			onAsyncCast: ((id: String, map: Map<String, Any?>, resolver: (T) -> Unit) -> Unit)? = null,
			onSuccess: (list: ArrayList<T>) -> Unit
		) {
			try {
				var ref: Query = db.collection(collection)
				where?.forEach { condition -> ref = ref.where(condition) }
				if (limit != null) ref = ref.limit(limit.toLong())

				ref.get().addOnCompleteListener { docs ->
					if (docs.isSuccessful) {
						val size = mutableIntStateOf(docs.result.size())
						val list = arrayListOf<T>()
						fun countDown() {
							size.intValue--
							if (size.intValue <= 0) {
								onSuccess(list)
							}
						}
						// there is no document
						if (docs.result.isEmpty) onSuccess(list)
						// iterate throughout the documents
						docs.result.forEach { document ->
							castDocument(document, onCast, onAsyncCast, ::countDown) { list.add(it) }
						}
					} else onError(R.string.unknown_error)
				}.addOnFailureListener {
					onError(R.string.unknown_error)
				}
			} catch (exception: Exception) {
				logcat(exception = exception)
				onError(R.string.unknown_error)
			}
		}

		fun insert(
			collection: String,
			data: HashMap<String, Any?>,
			onError: (error: Int) -> Unit,
			onSuccess: (id: String) -> Unit,
		) {
			try {
				db.collection(collection).add(data).addOnCompleteListener { inserting ->
					if (inserting.isSuccessful) onSuccess(inserting.result.id)
					else {
						val message = "Error inserting new document in the collection {$collection}"
						logcat(message, inserting.exception)
						onError(R.string.unknown_error)
					}
				}
			} catch (exception: Exception) {
				logcat(exception = exception)
				onError(R.string.unknown_error)
			}
		}

		fun updateOneByRef(
			ref: DocumentReference,
			data: HashMap<String, Any?>,
			onError: (error: Int) -> Unit,
			onSuccess: () -> Unit,
		) {
			try {
				ref.set(data, SetOptions.merge()).addOnCompleteListener { updating ->
					if (updating.isSuccessful) onSuccess()
					else {
						logcat("Error updating document by ref", updating.exception)
						onError(R.string.unknown_error)
					}
				}
			} catch (exception: Exception) {
				logcat(exception = exception)
				onError(R.string.unknown_error)
			}
		}

		fun deleteOneById(
			collection: String,
			id: String,
			onError: (error: Int) -> Unit,
			onSuccess: () -> Unit,
		) {
			try {
				refOf(collection, id).delete().addOnCompleteListener { deleting ->
					if (deleting.isSuccessful) onSuccess()
					else {
						logcat("Error deleting document by id {$id}", deleting.exception)
						onError(R.string.unknown_error)
					}
				}
			} catch (exception: Exception) {
				logcat(exception = exception)
				onError(R.string.unknown_error)
			}
		}

		private fun <T> castDocument(
			document: DocumentSnapshot,
			onCast: ((id: String, map: Map<String, Any?>) -> T)?,
			onAsyncCast: ((id: String, map: Map<String, Any?>, resolver: (T) -> Unit) -> Unit)? = null,
			countDown: (() -> Unit)? = null,
			onSuccess: (T) -> Unit
		) {
			if (onAsyncCast != null) {
				onAsyncCast(document.id, document.data!!) {
					onSuccess(it)
					countDown?.invoke()
				}
			} else if (onCast != null) {
				onSuccess(onCast(document.id, document.data!!))
				countDown?.invoke()
			}
		}
	}
}
