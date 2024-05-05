package com.example.academicpulse.utils

import android.content.Context

/** Note: An array type is used instead of Context to avoid null checks. We are certain that the context will not be null as saveAppContext is one of the first functions called in the lifecycle. */
private var appContextHolder = mutableListOf<Context>()

/** The App context that represents the active instance of the current activity; Which is **Index** as we have a single activity (Single Page App). */
var context: Context
	/** Return the App context which is `Index`, you can cast it to Index or LifecycleOwner as needed. */
	get() = appContextHolder[0]
	/** This setter will only be called in the `onCreate` method of the newly opened activity. (So, this function will not be utilized until another activity, aside from **Index**, is created.) */
	set(value) {
		if (appContextHolder.isEmpty()) appContextHolder.add(value)
		else appContextHolder[0] = value
	}
