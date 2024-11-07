package com.example.academicpulse.model


import com.example.academicpulse.utils.useCast
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class Notification(
	var isRead: Boolean = false,
	var document: String = "no",
	var collection: String = "0",
	var title: String = "",
	var createdAt: Timestamp = Timestamp(Date()),
	var isAcceptance : Boolean = false,
	var id: String = "0",
) {

	fun formatDate(date: Timestamp): String {
		val date1 = Date(date.seconds * 1000L + date.nanoseconds / 1000000L)
		val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
		return sdf.format(date1)
	}

	companion object {
		fun fromMap(map: Map<String, Any?>): Notification {
			return Notification(
				isRead = useCast(map, "isRead", false),
				id = useCast(map, "id", "0"),
				title = useCast(map, "title", ""),
				document = useCast(map, "document", "no-document"),
				collection = useCast(map, "collection", "publication"),
				isAcceptance = useCast(map, "isAcceptance", false),
			)
		}
	}
}