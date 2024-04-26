package com.example.academicpulse.router

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.academicpulse.view.pages.auth.*
import com.example.academicpulse.view.pages.home.*
import com.example.academicpulse.view.pages.inbox.*
import com.example.academicpulse.view.pages.profile.*
import kotlin.system.exitProcess

/** NavGraph is a schema that contains all the pages used in the App.
 * - Each page is declared with its instance, path key and back handler button behavior.
 */
@Composable
fun NavGraph(nav: NavHostController, startDestination: String) {
	NavHost(
		navController = nav,
		startDestination = startDestination,
		enterTransition = {
			fadeIn(animationSpec = tween(300)) + slideIntoContainer(
				AnimatedContentTransitionScope.SlideDirection.Left,
				tween(300)
			)
		},
		exitTransition = {
			fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
				AnimatedContentTransitionScope.SlideDirection.Left,
				tween(300)
			)
		},
		popEnterTransition = {
			fadeIn(animationSpec = tween(300)) + slideIntoContainer(
				AnimatedContentTransitionScope.SlideDirection.Right,
				tween(300)
			)
		},
		popExitTransition = {
			fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
				AnimatedContentTransitionScope.SlideDirection.Right,
				tween(300)
			)
		},
	) {
		navigation(route = "home", startDestination = "home/index") {
			composable(route = "home/index") {
				HomePage()
				BackHandler { exitProcess(0) }
			}
		}
		navigation(route = "inbox", startDestination = "inbox/index") {
			composable(route = "inbox/index") {
				InboxPage()
				BackHandler { Router.replace("home", true) }
			}
		}
		navigation(route = "profile", startDestination = "profile/index") {
			composable(route = "profile/index") {
				ProfilePage()
				BackHandler { Router.replace("home", true) }
			}
		}
		navigation(route = "auth", startDestination = "auth/sign-up") {
			composable(route = "auth/sign-up") {
				SignUpInstitutionPage()
				BackHandler { exitProcess(0) }
			}
			composable(route = "auth/sign-up-user") {
				SignUpUserPage()
				BackHandler { Router.back(/* to = auth/sign-up */) }
			}
			composable(route = "auth/confirm-email") {
				ConfirmEmailPage()
				BackHandler { Router.replace("home", true) }
			}
			composable(route = "auth/verified-email") {
				VerifiedEmailPage()
				BackHandler { Router.replace("home", true) }
			}
			composable(route = "auth/login-user") {
				LogInUserPage()
				BackHandler { exitProcess(0) }
			}
			composable(route = "auth/activate-account") {
				ActivateAccountPage()
				BackHandler { exitProcess(0) }
			}
		}
	}
}
