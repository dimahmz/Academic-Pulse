package com.example.academicpulse.router

import androidx.activity.compose.BackHandler
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

/** Graph is a schema that contains all the pages used in the App, each page with it's path key and back button handler behavior */
@Composable
fun NavGraph(nav: NavHostController, startDestination: String) {
	NavHost(navController = nav, startDestination = startDestination) {
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
				BackHandler {
					Router.back(/* to = auth/sign-up */)
				}
			}
			composable(route = "auth/verified-email") {
				VerifiedEmailPage()
				BackHandler { Router.replace("home", true) }
			}
		}
	}
}
