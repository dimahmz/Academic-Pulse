package com.example.academicpulse.router

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.academicpulse.view.pages.auth.*
import com.example.academicpulse.view.pages.home.*
import com.example.academicpulse.view.pages.notifications.*
import com.example.academicpulse.view.pages.profile.*
import com.example.academicpulse.view.pages.publication.*

/** NavGraph is a schema that contains all the pages used in the App.
 * - Each page is declared with its instance, path key and back handler button behavior.
 */
@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
	NavHost(
		navController = navController, startDestination = startDestination
	) {
		// Auth Routes
		composable(route = "auth/sign-in") {
			SignInPage(/* BackHandler, exit the app */)
		}
		composable(route = "auth/activation") {
			ActivationPage(/* BackHandler, exit the app */)
		}
		composable(route = "auth/sign-up-institution") {
			SignUpInstitutionPage(/* BackHandler, to = auth/sign-in */)
		}
		composable(route = "auth/sign-up") {
			SignUpPage(/* BackHandler, to = auth/sign-up-institution */)
		}
		composable(route = "auth/verification") {
			VerificationPage(/* BackHandler, to = auth/sign-in */)
		}

		// Unique Routes
		composable(route = "home") {
			HomePage()
			BackHandler { Router.exit() }
		}
		composable(route = "notifications") {
			NotificationPage()
			BackHandler { Router.navigate("home", true) }
		}

		// Profile Routes
		composable(route = "profile/index") {
			ProfilePage(/* BackHandler, replaceWith = home */)
		}
		composable(route = "profile/settings") {
			SettingsPage(/* BackHandler, to = profile/index */)
		}
		composable(route = "profile/about") {
			AboutPage(/* BackHandler, to = profile/settings */)
		}

		// Publications Routes
		composable(route = "publications/add-publication") {
			AddPublicationPage(/* BackHandler, to = profile/index */)
		}
		composable(route = "publications/one-publication") {
			OnePublicationPage(/* BackHandler, to = profile/index or home/index */)
		}
		composable(route = "publications/select-authors") {
			SelectAuthorsPage(/* BackHandler, to = publication/add-publication */)
		}
		composable(route = "publications/pdf-viewer") {
			PDFViewerPage(/* BackHandler, to = publication/one-publication */)
		}
	}
}
