package com.example.academicpulse.router

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Router(navigator: NavHostController) {
	private var navController = navigator
	private var route = MutableLiveData("home")
	private var navBarVisible = MutableLiveData(true)

	// Note: Static variables and methods are used just to hold the created Router instance and be accessible in anywhere without passing it via any component props
	companion object {
		// Note: An array type is used instead of Router to avoid null checks. We are certain that the app router will not be null as AppRouter is the second function called in the whole app after saveAppContext.
		private val appRouter = mutableListOf<Router>()

		@Composable
		fun AppRouter() {
			val navigator = rememberNavController()
			if (appRouter.isEmpty()) appRouter.add(Router(navigator))
		}

		fun getNavController(): NavHostController {
			return appRouter[0].navController
		}

		fun isNavBarVisible(): MutableLiveData<Boolean> {
			return appRouter[0].navBarVisible
		}

		fun setNavBarVisibleState(state: Boolean) {
			appRouter[0].navBarVisible.value = state
		}

		fun getRoute(): MutableLiveData<String> {
			return appRouter[0].route
		}

		fun navigate(target: String, navBarVisible: Boolean) {
			appRouter[0].navBarVisible.value = navBarVisible
			appRouter[0].route.value = target
			appRouter[0].navController.navigate(target)
		}

		fun replace(target: String, navBarVisible: Boolean) {
			appRouter[0].navBarVisible.value = navBarVisible
			appRouter[0].route.value = target
			appRouter[0].navController.navigate(target) {
				popUpTo(target) {
					inclusive = true
				}
			}
		}
	}
}
