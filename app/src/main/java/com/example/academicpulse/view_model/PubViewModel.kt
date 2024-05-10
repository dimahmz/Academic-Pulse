package com.example.academicpulse.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.AddPublicationInfo
import com.example.academicpulse.model.Field
import com.example.academicpulse.router.Router
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

class PublicationViewModel : ViewModel() {

	val addPubInfo: AddPublicationInfo = AddPublicationInfo()
	val auth = Firebase.auth

	fun saveNewPubInfo(title: Field, abstract: Field, doi : Field) {
		addPubInfo.title = title.value
		addPubInfo.abstract = abstract.value
		addPubInfo.doi = doi.value

	}

	fun clearAddNewPubForm() {
		addPubInfo.title = ""
		addPubInfo.abstract = ""
		addPubInfo.doi = ""
	}


	fun addNewPub(onFinish: (message : Int ,success : Boolean) -> Unit) {
		val user = auth.currentUser
		if (user != null) {
			// fetch the user collection
			val userRef = Store.database.collection("user").document(user.uid)
			userRef.get().addOnCompleteListener { userDoc ->
				if (userDoc.isSuccessful) {
					// add a new publications collection
					Store.database.collection("publication").add(addPubInfo.toMap())
						.addOnCompleteListener { pubRef ->
							if (pubRef.isSuccessful) {
								val publicationId = pubRef.result?.id
								// collection of publicationUIDs inside the User
								val userPublications =
									userDoc.result?.get("publications") as? ArrayList<String?> ?: ArrayList()
								// push the new
								userPublications.add(publicationId)
								val data = hashMapOf<String, Any>("publications" to userPublications)
								userRef.set(data, SetOptions.merge()).addOnCompleteListener { saving ->
									if (!saving.isSuccessful){
										Log.w("Error adding document", saving.exception)
										onFinish(R.string.unknown_error, false)
										// TODO :
										// if this fails we should remove the publication
									}
									// Finally everything went right (LOL)
									onFinish(0, true)
									clearAddNewPubForm()
									Router.navigate("profile/index", true)
								}
							} else {
								Log.w("Error adding document", pubRef.exception)
								onFinish(R.string.unknown_error, false)
							}
						}
				} else {
					Log.w("Error getting document", userDoc.exception)
					onFinish(R.string.unknown_error, true)
				}
			}
		}
	}
}