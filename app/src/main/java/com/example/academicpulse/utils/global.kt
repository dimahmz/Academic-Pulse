package com.example.academicpulse.utils

import android.content.Context
import androidx.compose.runtime.Composable
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

/** A Composable hook function that allows performing side effects in another Composable function.
 * It re-runs the effect function whenever any of the dependencies list change.
 * ```
 * Example usage:
 * val (checkbox1, checkOption1) = useState(false)
 * val (checkbox2, checkOption2) = useState(false)
 * useEffect(listOf(checkbox1, checkbox2)) { list ->
 * 	println("One of the checkboxes changed")
 * }
 * ```
 * @param list List of dependencies to watch for changes
 * @param effect The side effect function to run
 * @return A function that can be called to stop the effect from running
 */
@Composable
fun <T> useEffect(list: List<T>, effect: (list: List<T>) -> Unit): () -> Unit {
	// Ensure that keys only contain primitive values
	require(list.all { it is Int || it is Boolean || it is String || it == null }) {
		"All dependencies list items must be of type Int, Boolean, or String, or null"
	}
	// Create a state to track whether the effect has been killed
	val (killed, kill) = useState(false)
	if (!killed) {
		// Generate a simple string key for the useState value
		val joinList = list.joinToString(separator = "-")
		val (key, setKey) = useState(joinList)
		// Re-run the effect function whenever the key changes
		if (key != joinList) {
			setKey(joinList)
			effect(list)
		}
	}
	// Function to stop the effect from running
	return { kill(true) }
}
