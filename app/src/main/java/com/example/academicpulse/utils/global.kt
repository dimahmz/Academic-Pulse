package com.example.academicpulse.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

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

/** Creates a shallow copy of this ArrayList.
 * @return A new ArrayList with the same elements as this ArrayList.
 */
fun <T> ArrayList<T>.copy(): ArrayList<T> {
	return ArrayList(this.toMutableList())
}
