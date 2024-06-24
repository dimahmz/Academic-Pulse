package com.example.academicpulse.router

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.academicpulse.view.pages.auth.*
import com.example.academicpulse.view.pages.home.*
import com.example.academicpulse.view.pages.notifications.*
import com.example.academicpulse.view.pages.profile.*
import com.example.academicpulse.view.pages.publications.*

/** NavGraph is a schema that contains all the pages used in the App.
 * - Each page is declared with its instance, path key and comments indicate all pages that it can navigate to.
 */
@Composable
fun NavGraph(navController: NavHostController, startDestination: String) {
	NavHost(navController = navController, startDestination = startDestination) {
		// ---------------------------- At App Start -----------------------------
		// navigate: home || auth/activation || auth/sign-in

		// ---------------------------- Unique Routes ----------------------------
		composable(route = "home") {
			HomePage()
			// BackHandler = exit
			// PublicationArticle Click = navigate: publications/one-publication
		}
		composable(route = "notifications") {
			NotificationPage()
			// BackHandler = navigate: home
		}

		// ---------------------------- Auth Routes ----------------------------
		composable(route = "auth/sign-in") {
			SignInPage()
			// BackHandler = exit
			// Register now = navigate: auth/sign-up-institution
			// Log in = navigate: home || auth/activation
		}
		composable(route = "auth/activation") {
			ActivationPage()
			// BackHandler = exit
			// Log out = navigate: auth/sign-in
		}
		composable(route = "auth/sign-up-institution") {
			SignUpInstitutionPage()
			// BackHandler = back: auth/sign-in
			// Next || Skip = navigate: sign-up
		}
		composable(route = "auth/sign-up") {
			SignUpPage()
			// BackHandler = back: auth/sign-up-institution
			// Continue = navigate: auth/verification
		}
		composable(route = "auth/verification") {
			VerificationPage()
			// BackHandler || Back to Login = back: auth/sign-in
		}

		// ---------------------------- Profile Routes ----------------------------
		composable(route = "profile") {
			ProfilePage()
			// BackHandler = navigate: home
			// UserCard Settings Icon = navigate: profile/settings
			// UserCard Add research = navigate: publications/add-publication
			// PublicationArticle Click = navigate: publications/one-publication
		}
		composable(route = "profile/settings") {
			SettingsPage()
			// BackHandler = back: profile
			// About = navigate: profile/about
			// Log out = navigate: auth/sign-in
		}
		composable(route = "profile/about") {
			AboutPage()
			// BackHandler = back: profile/settings
		}

		// ---------------------------- Publications Routes ----------------------------
		composable(route = "publications/add-publication") {
			AddPublicationPage()
			// BackHandler = back: profile
			// Authors (Modify) = navigate: publications/select-authors
			// Add research = navigate: publications/one-publication
		}
		composable(route = "publications/select-authors") {
			SelectAuthorsPage()
			// BackHandler || Continue = back: publication/add-publication
		}
		composable(route = "publications/one-publication") {
			OnePublicationPage()
			// BackHandler || onDelete = back: home || profile
			// View PDF = navigate: publications/pdf-viewer
			// DOI = Intent: https://www.doi.org/{{ doi }}
		}
		composable(route = "publications/pdf-viewer") {
			PDFViewerPage()
			// BackHandler = back: publications/one-publication
		}
	}
}
