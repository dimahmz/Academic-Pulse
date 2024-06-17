package com.example.academicpulse.router

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.academicpulse.Index
import com.example.academicpulse.utils.context
import com.example.academicpulse.utils.useAtom

class Router private constructor(private var navController: NavHostController) {
	private var route = MutableLiveData("auth")
	private var navBarVisible = MutableLiveData(false)

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
			val route = useAtom(appRouter[0].route, "auth")
			val navBarVisible = useAtom(appRouter[0].navBarVisible, false)
			return NavBar(route = route, navBarVisible = navBarVisible)
		}

		/** NavGraph is a schema that contains all the pages used in the App.
		 * - Each page is declared with its instance, path key and back handler button behavior.
		 */
		@Composable
		fun NavGraph() {
			val route = appRouter[0].route.value ?: "auth"
			NavGraph(navController = appRouter[0].navController, startDestination = route)
		}

		/** Use a normal navigation by adding the current page to backstack and redirect to the next page. */
		fun navigate(target: String, navBarVisible: Boolean) {
			appRouter[0].navBarVisible.value = navBarVisible
			appRouter[0].route.value = target
			appRouter[0].navController.navigate(target)
		}

		/** Navigate to a previous page. */
		fun back(navBarVisible: Boolean, step: Int = 1, target: String? = null) {
			appRouter[0].navBarVisible.value = navBarVisible
			if (target != null) appRouter[0].route.value = target
			repeat(step) { appRouter[0].navController.popBackStack() }
		}

		/** Exit the app. */
		fun exit() {
			(context as Index).finish()
		}
	}
}
