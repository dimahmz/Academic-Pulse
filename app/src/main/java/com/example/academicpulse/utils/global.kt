package com.example.academicpulse.utils

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

fun <T> useCast(value: Any?, defaultValue: T): T {
	return if (value != null) (value as T) else defaultValue
}

fun <T> useCast(map: DocumentSnapshot?, key: String, defaultValue: T): T {
	return useCast(map?.get(key), defaultValue)
}

fun <T> useCast(map: Map<String, Any?>?, key: String, defaultValue: T): T {
	return useCast(map?.get(key), defaultValue)
}

/** Log a message or an exception to the Logcat console. */
fun logcat(message: String? = null, exception: Exception? = null) {
	if (exception == null) Log.d("Academic Pulse", message ?: "")
	else Log.e("Academic Pulse", message ?: exception.message, exception)
}
