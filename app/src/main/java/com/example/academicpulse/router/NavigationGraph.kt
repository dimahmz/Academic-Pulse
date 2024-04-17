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
import com.example.academicpulse.view.pages.home.HomePage
import com.example.academicpulse.view.pages.inbox.InboxPage
import com.example.academicpulse.view.pages.profile.ProfilePage
import com.example.academicpulse.view_model.HomeViewModel
import com.example.academicpulse.view_model.InboxViewModel
import com.example.academicpulse.view_model.ProfileViewModel

@Composable
fun NavigationGraph() {
	NavHost(navController = Router.getNavController(), startDestination = "home") {
		navigation(route = "home", startDestination = "index") {
			composable(route = "index") {
				val viewModel = it.sharedViewModel<HomeViewModel>(Router.getNavController())
				HomePage(viewModel)
			}
		}
		navigation(route = "inbox", startDestination = "index") {
			composable(route = "index") {
				val viewModel = it.sharedViewModel<InboxViewModel>(Router.getNavController())
				InboxPage(viewModel)
			}
		}
		navigation(route = "profile", startDestination = "index") {
			composable(route = "index") {
				val viewModel = it.sharedViewModel<ProfileViewModel>(Router.getNavController())
				ProfilePage(viewModel)
			}
		}
	}
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
	val navGraphRoute = destination.parent?.route ?: return viewModel()
	val parentEntry = remember(this) {
		navController.getBackStackEntry(navGraphRoute)
	}
	return viewModel(parentEntry)
}
