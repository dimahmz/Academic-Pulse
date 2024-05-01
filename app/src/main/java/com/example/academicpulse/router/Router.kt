package com.example.academicpulse.router

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Router(navigator: NavHostController) {
	private var navController = navigator
	private var route = MutableLiveData("home")
	private var navBarVisible = MutableLiveData(true)

	// Note: Static variables and methods are used just to hold the global Router instance and be accessible in anywhere.
	companion object {
		// Note: An array type is used instead of Router to avoid null checks. We are certain that the app router will not be null as Provider method is one of the first functions called in the lifecycle.
		private val appRouter = mutableListOf<Router>()

		/** Provider initialize the router instance that will be used across the entire App */
		@Composable
		fun Provider() {
			if (appRouter.isEmpty()) {
				val navigator = rememberNavController()
				appRouter.add(Router(navigator))
			}
		}

		/** Bottom NavBar UI element containing main root routes with their icons, allowing direct navigation to them */
		@Composable
		fun NavBar(startDestination: String) {
			if (startDestination !== appRouter[0].route.value) {
				appRouter[0].route.value = startDestination
				appRouter[0].navBarVisible.value = false
			}
			return NavBar()
		}

		/** NavGraph is a schema that contains all the pages used in the App.
		 * - Each page is declared with its instance, path key and back handler button behavior.
		 */
		@Composable
		fun NavGraph(startDestination: String) {
			NavGraph(appRouter[0].navController, startDestination)
		}

		fun isNavBarVisible(): MutableLiveData<Boolean> {
			return appRouter[0].navBarVisible
		}

		fun getRoute(): MutableLiveData<String> {
			return appRouter[0].route
		}

		/** Use a normal navigation by adding the current page to backstack and redirect to the next page. */
		fun navigate(target: String, navBarVisible: Boolean) {
			appRouter[0].navBarVisible.value = navBarVisible
			appRouter[0].route.value = target
			appRouter[0].navController.navigate(target)
		}

		/** Navigate to the next page without adding the current page to backstack. */
		fun replace(target: String, navBarVisible: Boolean) {
			appRouter[0].navBarVisible.value = navBarVisible
			appRouter[0].route.value = target
			appRouter[0].navController.navigate(target) {
				popUpTo(target) {
					inclusive = true
				}
			}
		}

		/** Navigate to a previous page. */
		fun back(step: Int = 1) {
			repeat(step) { appRouter[0].navController.popBackStack() }
		}
	}
}
