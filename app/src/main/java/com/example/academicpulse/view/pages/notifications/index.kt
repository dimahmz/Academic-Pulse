package com.example.academicpulse.view.pages.notifications

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.model.Notification
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view.pages.notifications.components.EmptyNotificationMessage
import com.example.academicpulse.view.pages.notifications.components.NotificationItem
import com.example.academicpulse.view_model.Store


@Composable
fun NotificationPage() {

	val (showEmptyNotifications, setShowEmptyNotifications) = useState { false }
	val (isLoading, setIsLoading) = useState { false }
	val (userNotifications, setUserNotifications) = useState { arrayListOf<Notification>() }
	val liveUserNotifications by Store.notifications.userNotifications.collectAsState()

	// update the notifications UI depending on the data retrieved
	fun updateNotificationsUI(listOfNotifications: ArrayList<Notification>) {
		if (listOfNotifications.size == 0) {
			setShowEmptyNotifications(true)
		} else {
			setShowEmptyNotifications(false)
			setUserNotifications(listOfNotifications)
		}
	}

	// launch whenever the notifications change
	LaunchedEffect(liveUserNotifications) {
		// when it's already launched or the notifications was fetched
		if (Store.notifications.retrievedNotification.value == true) {
			updateNotificationsUI(Store.notifications.userNotifications.value)
			return@LaunchedEffect
		}
		// launched for the first time
		setIsLoading(true)
		Store.notifications.getUserNotifications(onError = {
			setShowEmptyNotifications(true)
			setIsLoading(false)
		}, onServerError = {
			Store.applicationState.ShowServerErrorAlertDialog()
			setIsLoading(false)
			setShowEmptyNotifications(true)
		}, onSuccess = {
			updateNotificationsUI(it)
			setIsLoading(false)
		})
	}


	// component UI
	LazyColumn(Modifier.padding(bottom = bottomBarHeight)) {
		if (isLoading) {
			item {
				Spinner()
			}
		} else {
			if (showEmptyNotifications) {
				item {
					Box(modifier = Modifier.padding(vertical = 50.dp)) {
						EmptyNotificationMessage()
					}
				}
			} else {
				items(userNotifications, key = { it.id }) {
					NotificationItem(it)
					Line(height = 1.dp)
				}
			}
		}
	}

	BackHandler { Router.navigate("home") }
}
