package com.example.academicpulse.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.academicpulse.view.components.Page
import com.example.academicpulse.view_model.HomeViewModel

@Composable
fun NavigationGraph(router: Router) {
	NavHost(navController = router.navController, startDestination = "home") {
		navigation(route = "home", startDestination = "home") {
			composable("home") {
				val sharedViewModel = it.sharedViewModel<HomeViewModel>(router.navController)
				Page(sharedViewModel, router, "home", "home", "article/45")
			}
			composable("article/45") {
				val sharedViewModel = it.sharedViewModel<HomeViewModel>(router.navController)
				Page(sharedViewModel, router, "home", "article/45", "articles")
			}
		}
	}
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): HomeViewModel {
//	val navGraphRoute = destination.parent?.route ?: return viewModel()
//	val parentEntry = remember(this) {
//		navController.getBackStackEntry(navGraphRoute)
//	}
//	return viewModel(parentEntry)
	return HomeViewModel()
}
