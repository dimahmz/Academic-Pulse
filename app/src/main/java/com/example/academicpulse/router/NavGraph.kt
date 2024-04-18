package com.example.academicpulse.router

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.academicpulse.view.pages.home.HomePage
import com.example.academicpulse.view.pages.inbox.InboxPage
import com.example.academicpulse.view.pages.profile.ProfilePage
import com.example.academicpulse.view_model.HomeViewModel
import com.example.academicpulse.view_model.InboxViewModel
import com.example.academicpulse.view_model.ProfileViewModel
import kotlin.system.exitProcess

/** Graph is a schema that contains all the pages used in the App, each page with it's path key and back button handler behavior */
@Composable
fun NavGraph(nav: NavHostController) {
	NavHost(navController = nav, startDestination = "home") {
		navigation(route = "home", startDestination = "home/index") {
			composable(route = "home/index") {
				val viewModel = it.sharedViewModel<HomeViewModel>(nav)
				HomePage(viewModel)
				BackHandler {
					exitProcess(0)
				}
			}
		}
		navigation(route = "inbox", startDestination = "inbox/index") {
			composable(route = "inbox/index") {
				val viewModel = it.sharedViewModel<InboxViewModel>(nav)
				InboxPage(viewModel)
				BackHandler {
					Router.replace("home", true)
				}
			}
		}
		navigation(route = "profile", startDestination = "profile/index") {
			composable(route = "profile/index") {
				val viewModel = it.sharedViewModel<ProfileViewModel>(nav)
				ProfilePage(viewModel)
				BackHandler {
					Router.replace("home", true)
				}
			}
		}
	}
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(nav: NavController): T {
	val navGraphRoute = destination.parent?.route ?: return viewModel()
	val parentEntry = remember(this) {
		nav.getBackStackEntry(navGraphRoute)
	}
	return viewModel(parentEntry)
}
