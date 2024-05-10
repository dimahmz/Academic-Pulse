package com.example.academicpulse.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.DB.Publication
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.atomic.AtomicInteger

class ProfileViewModel : ViewModel() {

	val auth = Firebase.auth
	val userPublications = MutableLiveData(arrayListOf<Publication>())


	fun fetchUserPublication(onFinish: (message: Int, success: Boolean) -> Unit) {
		val user = auth.currentUser
		if (user != null) {
			Log.w("Error getting document", "User not found")
			onFinish(R.string.unknown_error, true)
		}
		// fetch the user collection
		val userRef = Store.database.collection("user").document(user!!.uid)

		userRef.get().addOnCompleteListener { userDoc ->
			if (!userDoc.isSuccessful) {
				Log.w("Error getting document", userDoc.exception)
				onFinish(R.string.unknown_error, true)
			}
			// store publication UIDs
			val userPublicationsUIDs =
				userDoc.result?.get("publications") as? ArrayList<String?> ?: ArrayList()

			val fetchCount = AtomicInteger(userPublicationsUIDs.size)

			// clear the user publication
			userPublications.value?.clear();

			// fetch every user's publication
			userPublicationsUIDs.forEach { publicationId ->
				val publicationRef = Store.database.collection("publication").document(publicationId!!)
				publicationRef.get().addOnCompleteListener { pubSnapshot ->
					if (!pubSnapshot.isSuccessful) {
						Log.w("Error getting document", userDoc.exception)
						onFinish(R.string.unknown_error, false)
					}
					val publicationMap = pubSnapshot.result.data

					val onePublication = Publication.fromMap(publicationMap)
					userPublications.value?.add(onePublication)
					// everything went OK!!!
					if (fetchCount.decrementAndGet() == 0) {
						Log.v("onFinish", "Pub size :" + userPublications.value?.size)
						onFinish(R.string.success_fetch, true)

					}
				}
			}
		}
	}
}
