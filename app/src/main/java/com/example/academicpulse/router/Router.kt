package com.example.academicpulse.router

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController

class Router(context: Context) {
	val navController = NavHostController(context)
	val root = MutableLiveData(navbarItems[0].root)
	private val route = MutableLiveData(navbarItems[0].destination)
	var navbarVisible = MutableLiveData(true)

	fun redirect(route: String, root: String, navbarVisible: Boolean? = null) {
		this.route.value = route
		this.root.value = root
		var autoVisibility = navbarVisible == null
		if (autoVisibility) {
			autoVisibility = false
			for (it in navbarItems) {
				if (root == it.root && route == it.destination) {
					autoVisibility = true
					break
				}
			}
			this.navbarVisible.value = autoVisibility
		} else this.navbarVisible.value = navbarVisible
		navController.navigate(route)
	}
}
