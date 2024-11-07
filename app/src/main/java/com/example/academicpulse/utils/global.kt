package com.example.academicpulse.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date

inline fun <reified T> useCast(value: Any?, defaultValue: T): T {
	return if (value != null) (value as T) else defaultValue
}

@SuppressLint("SimpleDateFormat")
fun stringToDate(dateString: String, pattern: String): Date {
	val formatter = SimpleDateFormat(pattern)
	return formatter.parse(dateString)!!
}

inline fun <reified T> useCast(map: Map<String, Any?>?, key: String, defaultValue: T): T {
	return useCast(map?.get(key), defaultValue)
}

/** Log a message or an exception to the Logcat console. */
fun logcat(message: String? = null, exception: Exception? = null) {
	if (exception == null) Log.d("Academic Pulse", message ?: "")
	else Log.e("Academic Pulse", message ?: exception.message, exception)
}
