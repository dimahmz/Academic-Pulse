package com.example.academicpulse.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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

/** Adds the given observer to the observers list within the lifespan of the given owner.
 * Observe a LiveData object and invokes a callback function whenever the LiveData's value changes.
 * This function provides a shorter way to use lifeData.observe by automatically handling the lifecycle owner.
 * ```
 * Example usage:
 * val myLiveData = MutableLiveData<String>()
 * myLiveData.observe(value ->
 * 	println("LiveData value changed: $value")
 * )
 * ```
 * @param callback The observer that will receive the events when the value changes.
 */
fun <T> LiveData<T>.observe(callback: (T) -> Unit) {
	this.observe(context as LifecycleOwner) { callback(it) }
}
