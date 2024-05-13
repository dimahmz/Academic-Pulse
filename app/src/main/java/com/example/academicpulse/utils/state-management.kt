package com.example.academicpulse.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData

/** A composable hook function used to create and manage state within another Composable function.
 * It returns a Pair containing the current state value and a function to update the state.
 * - Note: We use function type here to wrap the value to execute it one time. (To avoid generating useless instances ...etc if it is not a primitive value)
 * ```
 * Example usage:
 * val (counter, setCounter) = useState { 0 }
 *
 * Column {
 * 	Text(text = "Counter: $counter")
 * 	Button(text = "Increase") { setCounter(counter + 1) }
 * }
 * ```
 * @param value The initial value of the state
 * @return A Pair containing the current state value and a function to update it
 */
@Composable
fun <T> useState(value: @DisallowComposableCalls () -> T): Pair<T, (T) -> Unit> {
	// Create and remember a mutable state variable initialized with the provided data
	val state = remember { mutableStateOf(value()) }
	// Function to update the state
	fun setData(value: T) {
		val oldValue = state.value
		if (value != oldValue) state.value = value
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
 * 	val unreadCount = MutableLiveData(10)
 * 	fun clearAll() {
 * 		database.mutation("example to clear").addOnSuccessListener { unreadCount.value = 0 }
 * 	}
 * }
 *
 * val count = useAtom(Store.notifications.unreadCount, 0)
 *
 * Column {
 * 	Text(text = "Unread notifications: $count")
 * 	Button(text = "Read All") { Store.notifications.clearAll() }
 * }
 * ```
 * @param liveData The LiveData object to observe
 * @return The current value of the observed LiveData
 */
@Composable
fun <T> useAtom(liveData: LiveData<T>, defaultValue: T): T {
	return liveData.observeAsState(defaultValue).value
}

@Composable
fun <T> useAtom(liveData: LiveData<T>): T? {
	return liveData.observeAsState().value
}
