package com.example.academicpulse.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academicpulse.R
import com.example.academicpulse.model.Notification
import com.example.academicpulse.utils.logcat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.MutableStateFlow

class Notifications : ViewModel() {
	private val realtimeDB = Firebase.database
	private val database = Firebase.database.reference
	val retrievedNotification = MutableLiveData(false)
	val unReadNotificationsTotal = MutableStateFlow<Int>(0)
	val userNotifications = MutableStateFlow(arrayListOf<Notification>())


	fun getUserNotifications(
		onSuccess: (ArrayList<Notification>) -> Unit, onError: (error: Int) -> Unit
	) {
		// get the current user
		Store.users.getCurrent(onError = { it -> onError(it) }, onSuccess = { user, ref ->
			// Listen to all the changes inside this user's notifications
			val userNotificationsRef = realtimeDB.getReference("/users/${user.id}/notifications")
			userNotificationsRef.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(dataSnapshot: DataSnapshot) {
					val userNotificationsList = ArrayList<Notification>()
					// counter for notifications size
					var counter: Int = dataSnapshot.children.count()
					var unReadNotificationsCounter = 0

					// Function to check if all asynchronous operations are complete
					fun checkCompletion() {
						if (counter == 0) {
							retrievedNotification.value = true
							userNotifications.value = userNotificationsList
							unReadNotificationsTotal.value = unReadNotificationsCounter
							logcat(unReadNotificationsTotal.value.toString())
							onSuccess(userNotificationsList)
						}
					}
					// iterate through the user notifications snapshot
					for (notificationSnapshot in dataSnapshot.children) {
						if (!notificationSnapshot.exists() || notificationSnapshot.key == null) {
							counter--
							checkCompletion()
						} else {
							// get each notifications
							database.child("notifications").child("${notificationSnapshot.key}").get()
								.addOnSuccessListener {
									counter--
									if (it.exists()) {
										val notification = it.getValue<Notification>()
										if (notification?.collection == "publication") {
											notification.id = it.key!!
											// boolean values are not casted propertly
											val isRead =
												notificationSnapshot.child("isRead").getValue(Boolean::class.java) ?: false
											notification.isRead = isRead
											if (!isRead) unReadNotificationsCounter++
											notification.isAcceptance =
												it.child("isAcceptance").getValue(Boolean::class.java) ?: false
											// LAST IN FIRST OUT
											// I don't wanna use a Stack
											userNotificationsList.add(0, notification)
										}
									}
									checkCompletion()
								}.addOnFailureListener {
									counter--
									checkCompletion()
								}
						}
					}
				}

				override fun onCancelled(error: DatabaseError) {
					logcat(error.message)
					onError(R.string.unknown_error)
				}
			})

		})
	}

	fun markNotificationAsRead(notificationId: String) {
		if (Store.users.current.value?.id == null) return
		database.child("users").child(Store.users.current.value!!.id).child("notifications")
			.child(notificationId).child("isRead").setValue(true)
	}
}
