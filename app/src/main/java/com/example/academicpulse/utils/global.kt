package com.example.academicpulse.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/** The App context that represents the active instance of the current activity which is `Index` as we have a single activity (Single Page App).
 * Note: An array type is used instead of Context to avoid null checks. We are certain that the context will not be null as saveAppContext is one of the first functions called in the lifecycle.
 */
private var appContext = mutableListOf<Context>()
fun getAppContext(): Context {
	return appContext[0]
}

/** This function will only be called in the `onCreate` method of the newly opened activity. (So, this function will not be utilized until another activity, aside from `Index`, is created.) */
fun saveAppContext(context: Context) {
	appContext.add(context)
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
	this.observe(getAppContext() as LifecycleOwner) { callback(it) }
}

fun logcat(message: String, exception: Exception? = null) {
	if (exception == null) Log.d("Academic Pulse", message)
	else Log.e("Academic Pulse", message, exception)
}
