package com.example.academicpulse.utils

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/** The App context that represents the active instance of the current activity which is `Index` as we have a single activity (Single Page App).
 * Note: An array type is used instead of Context to avoid null checks. We are certain that the context will not be null as saveAppContext the first function called in the whole app.
 */
private var appContext = mutableListOf<Context>()
fun getAppContext(): Context {
	return appContext[0]
}

/** This function will only be called in the `onCreate` method of the newly opened activity. (So, this function will not be utilized until another activity, aside from `Index`, is created.) */
fun saveAppContext(context: Context) {
	appContext.add(context)
}

/** Observe a LiveData object and invokes a callback function whenever the LiveData's value changes.
 * This function provides a shorter way to use lifeData.observe by automatically handling the lifecycle owner.
 * ```
 * Example usage:
 * val myLiveData = MutableLiveData<String>()
 * useObserve(myLiveData) { value ->
 *     println("LiveData value changed: $value")
 * }
 * ```
 * @param lifeData The LiveData object to observe
 * @param callback The callback function to invoke when the LiveData's value changes
 */
fun <T> useObserve(lifeData: LiveData<T>, callback: (T) -> Unit) {
	lifeData.observe(getAppContext() as LifecycleOwner) { callback(it) }
}

/** A composable hook function used to create and manage state within another Composable function.
 * It returns a Pair containing the current state value and a function to update the state.
 * ```
 * Example usage:
 * val (counter, setCounter) = useState(0)
 *
 * Column {
 * 	Text("Counter: $counter")
 * 	Button(onClick = { setCounter(counter + 1) }) {
 * 		Text("Increase")
 * 	}
 * }
 * ```
 * @param value The initial value of the state
 * @return A Pair containing the current state value and a function to update it
 */
@Composable
fun <T> useState(value: T): Pair<T, (T) -> Unit> {
	// Create and remember a mutable state variable initialized with the provided data
	val state = remember { mutableStateOf(value) }

	// Function to update the state
	fun setData(data: T) {
		state.value = data
	}
	// Return the current state value and the function to update it
	return Pair(state.value, ::setData)
}

/** A composable hook function similar to useState, used to observe a LiveData object and update a state variable within another Composable function.
 * However, instead of returning a setter function within a Pair, it directly observes the LiveData object and updates the state variable when the LiveData changes.
 * It is mainly used for variables that already have a built-in setter that can be used across the app.
 * ```
 * Example usage:
 * class NotificationsViewModel: ViewModel {
 * 	val unreadCount: MutableLiveData(0)
 * 	fun clearAll() {
 * 		database.mutation("example to clear").addOnSuccessListener { unreadCount.value = 0 }
 * 	}
 * }
 *
 * val notificationsViewModel = NotificationsViewModel()
 * val count = useAtom(notificationsViewModel.unreadCount)
 *
 * Column {
 * 	Text("Unread notifications: $count")
 * 	Button(onClick = { notificationsViewModel.clearAll() }) {
 * 		Text("Read All")
 * 	}
 * }
 * ```
 * @param lifeData The LiveData object to observe
 * @return The current value of the observed LiveData
 */
@Composable
fun <T> useAtom(lifeData: LiveData<T>): T? {
	// Create and remember a mutable state variable initialized with the current value of the LiveData
	val state = remember { mutableStateOf(lifeData.value) }
	// Observe the LiveData and update the state variable when it changes
	useObserve(lifeData) { state.value = it }
	// Return the current value of the state variable
	return state.value
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
 * @param effect The side effect function to run
 * @param list List of dependencies to watch for changes
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
		// Generate a unique key for each combination of keys
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
