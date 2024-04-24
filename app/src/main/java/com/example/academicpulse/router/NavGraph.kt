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
import com.example.academicpulse.view.pages.auth.*
import com.example.academicpulse.view.pages.home.*
import com.example.academicpulse.view.pages.inbox.*
import com.example.academicpulse.view.pages.profile.*
import com.example.academicpulse.view_model.AuthViewModel
import com.example.academicpulse.view_model.HomeViewModel
import com.example.academicpulse.view_model.InboxViewModel
import com.example.academicpulse.view_model.ProfileViewModel
import kotlin.system.exitProcess

/** Graph is a schema that contains all the pages used in the App, each page with it's path key and back button handler behavior */
@Composable
fun NavGraph(nav: NavHostController, startDestination: String) {
	NavHost(navController = nav, startDestination = startDestination) {
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
		navigation(route = "auth", startDestination = "auth/sign-up") {
			composable(route = "auth/sign-up") {
				val viewModel = it.sharedViewModel<AuthViewModel>(nav)
				SignUpInstitutionPage(viewModel)
				BackHandler {
					exitProcess(0)
				}
			}
			composable(route = "auth/sign-up-user") {
				val viewModel = it.sharedViewModel<AuthViewModel>(nav)
				SignUpUserPage(viewModel)
				BackHandler {
					Router.back()
				}
			}
			composable(route = "auth/verified-email") {
				val viewModel = it.sharedViewModel<AuthViewModel>(nav)
				VerifiedEmailPage(viewModel)
				BackHandler {
					Router.replace("home", true)
				}
			}
			composable(route = "auth/confirm-email") {
				val viewModel = it.sharedViewModel<AuthViewModel>(nav)
				ConfirmEmailPage(viewModel)
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
