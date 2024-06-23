package com.example.academicpulse.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.academicpulse.Index
import com.example.academicpulse.utils.context

class Router private constructor(private var navController: NavHostController) {
	private val startDestination = "auth/sign-in"
	private var route = mutableStateOf(startDestination)

	// Note: Static variables and methods are used just to hold the global Router instance and be accessible in anywhere.
	companion object {
		// Note: An array type is used instead of Router to avoid null checks. We are certain that the app router will not be null as Provider method is one of the first functions called in the lifecycle.
		private val appRouter = mutableListOf<Router>()

		/** Provider initialize the router instance that will be used across the entire App */
		@Composable
		fun Provider() {
			val controller = rememberNavController()
			if (appRouter.isEmpty())
				appRouter.add(Router(controller))
			else appRouter[0].navController = controller
		}

		/** Bottom NavBar UI element containing main root routes with their icons, allowing direct navigation to them */
		@Composable
		fun NavBar() {
			return NavBar(appRouter[0].route.value)
		}

		/** NavGraph is a schema that contains all the pages used in the App.
		 * - Each page is declared with its instance, path key and comments indicate all pages that it can navigate to.
		 */
		@Composable
		fun NavGraph() {
			NavGraph(appRouter[0].navController, appRouter[0].startDestination)
		}

		/** Use a normal navigation by adding the current page to backstack and redirect to the next page. */
		fun navigate(target: String) {
			appRouter[0].route.value = target
			appRouter[0].navController.navigate(target)
		}

		/** Navigate to a previous page. */
		fun back(target: String, step: Int = 1) {
			appRouter[0].route.value = target
			repeat(step) { appRouter[0].navController.popBackStack() }
		}

		/** Exit the app. */
		fun exit() {
			(context as Index).finish()
		}
	}
}
