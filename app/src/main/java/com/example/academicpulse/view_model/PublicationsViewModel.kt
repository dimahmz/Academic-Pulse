package com.example.academicpulse.view_model

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.Publication
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useCast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class PublicationsViewModel(private val db: FirebaseFirestore, private val auth: FirebaseAuth) :
	ViewModel() {
	val userPublications = MutableLiveData(arrayListOf<Publication>())
	val selectedPublication = MutableLiveData(Publication())

	fun getUserPublications(onSuccess: () -> Unit, onError: (error: Int) -> Unit) {
		val user = auth.currentUser ?: return onError(R.string.unknown_error)
		db.collection("user").document(user.uid).get().addOnCompleteListener { userDoc ->
			if (!userDoc.isSuccessful) {
				logcat("Error getting document", userDoc.exception)
				return@addOnCompleteListener onError(R.string.unknown_error)
			}
			val userPublicationsUIDs = useCast(userDoc.result, "publications", arrayListOf<String>())
			val newUserPublicationsList = arrayListOf<Publication>()
			val size = mutableIntStateOf(userPublicationsUIDs.size)

			userPublicationsUIDs.forEach { publicationId ->
				val publicationRef = db.collection("publication").document(publicationId)
				publicationRef.get().addOnCompleteListener { pubDoc ->
					if (!pubDoc.isSuccessful) {
						logcat("Error getting document", userDoc.exception)
						onError(R.string.unknown_error)
					} else newUserPublicationsList.add(Publication.fromMap(pubDoc.result.data))
					size.intValue--
					if (size.intValue == 0) {
						userPublications.value = newUserPublicationsList
						onSuccess()
					}
				}
			}
		}
	}

	fun insert(publication: Publication, onError: (error: Int) -> Unit) {
		val user = auth.currentUser ?: return onError(R.string.unknown_error)
		// fetch the user collection
		val userRef = db.collection("user").document(user.uid)
		userRef.get().addOnCompleteListener { userDoc ->
			if (!userDoc.isSuccessful) {
				logcat("Error getting document", userDoc.exception)
				return@addOnCompleteListener onError(R.string.unknown_error)
			}
			// add a new publications collection
			db.collection("publication").add(publication.toMap()).addOnCompleteListener { pubDoc ->
				if (pubDoc.isSuccessful) {
					val userPublications = useCast(userDoc.result, "publications", arrayListOf<String?>())
					userPublications.add(pubDoc.result.id)
					val data = hashMapOf<String, Any>("publications" to userPublications)
					userRef.set(data, SetOptions.merge()).addOnCompleteListener { saving ->
						if (!saving.isSuccessful) {
							logcat("Error adding document", saving.exception)
							onError(R.string.unknown_error)
							// TODO : if this fails we should remove the publication
						} else Router.back()
					}
				} else {
					logcat("Error adding document", pubDoc.exception)
					onError(R.string.unknown_error)
				}
			}
		}
	}

	fun getOneById(id: String, onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
		val publicationRef = db.collection("publication").document(id)
		publicationRef.get().addOnCompleteListener { pubDoc ->
			if (!pubDoc.isSuccessful) {
				logcat("Error getting document", pubDoc.exception)
				onError(R.string.unknown_error)
			} else {
				selectedPublication.value = Publication.fromMap(pubDoc.result.data)
				logcat(selectedPublication.value.toString())
				onSuccess()
			}
		}
	}
}
