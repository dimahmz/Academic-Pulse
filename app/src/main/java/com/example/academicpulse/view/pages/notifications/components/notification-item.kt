package com.example.academicpulse.view.pages.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.Notification
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.theme.unReadNotificationIcon
import com.example.academicpulse.theme.unReadNotificationIconBorder
import com.example.academicpulse.view.components.basic.Description
import com.example.academicpulse.view.components.basic.H3
import com.example.academicpulse.view.components.basic.Image
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view_model.Store

import com.google.firebase.Timestamp


@Composable
fun NotificationItem(notification: Notification) {
	if (notification.collection == "publication") {
		Column(verticalArrangement = Arrangement.spacedBy(4.dp),
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = pagePaddingX, horizontal = (pagePaddingX.value / 2).dp)
				.clickable {
					if (notification.collection == "publication") {
						Store.notifications.markNotificationAsRead(notification.id)
						Store.publications.selectedPublicationId = notification.document
						Router.navigate("publications/one-publication")
					}

				}) {
			if (!notification.isRead) {
				Box(
					modifier = Modifier
						.align(Alignment.Start)
						.padding(2.dp) // Adjust padding as needed
						.size(8.dp) // Size of the dot
						.background(unReadNotificationIcon, CircleShape) // Dot color and shape
						.border(
							width = 1.dp,  // Border width
							color = unReadNotificationIconBorder,  // Border color
							shape = CircleShape  // Circle shape
						)
				)
			}
			Row {
				Row(verticalAlignment = Alignment.CenterVertically) {
					if (notification.isAcceptance) Image(
						id = R.drawable.icon_status_accepted, size = 30.dp
					)
					else Image(id = R.drawable.icon_status_rejected, size = 30.dp)
				}
				Column(modifier = Modifier.padding(horizontal = 10.dp)) {
					Text(text = R.string.your_publication)
					H3(text = notification.title, maxLines = 2)
					if (notification.isAcceptance) Text(text = R.string.publication_accepted)
					else Text(text = R.string.publication_rejected)
					Spacer(Modifier.width(4.dp))
					Description(text = notification.formatDate(notification.createdAt))
				}
			}
		}
	}
}


@Preview(showBackground = true)
@Composable
fun NotificationItemPreview() {
	val notification = Notification(
		isRead = false,
		document = "publication",
		collection = "3LOPaEzPPt6nJiNOSm97",
		title = "DoS attack mitigation in SDN networks using a deeply programmable packet-switching node based on a hybrid FPGA/CPU data plane architecture",
		createdAt = Timestamp.now(),
		isAcceptance = true,
		id = "1"
	)
	NotificationItem(notification)
}