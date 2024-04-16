package com.example.academicpulse.view.layout_components

import android.annotation.SuppressLint
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.example.academicpulse.R
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.appContext
import com.example.academicpulse.utils.useAtom

class Router {
	private data class NavbarItem(
		val root: String,
		val title: Int,
		val icon: Int,
		var count: Int = 0,
	)

	companion object {
		@SuppressLint("StaticFieldLeak")
		private var navController: NavHostController? = null
		private val route: MutableLiveData<String> = MutableLiveData("home")
		private val root: MutableLiveData<String> = MutableLiveData("home")
		private var navbarVisible = MutableLiveData(true)
		private val navbarItems = listOf(
			NavbarItem("home", R.string.title_home, R.drawable.icon_home),
			NavbarItem("inbox", R.string.title_inbox, R.drawable.icon_inbox),
			NavbarItem("profile", R.string.title_profile, R.drawable.icon_profile),
		)

		fun init() {
			navController = navController ?: appContext?.let { NavHostController(it) }
		}

		fun redirect(route: String, root: String? = null, navbarVisible: Boolean? = null) {
			Router.route.value = route
			Router.root.value = root ?: route
			Router.navbarVisible.value = navbarVisible ?: (route === (root ?: route))
		}

		@Composable
		fun NavBar() {
			val navbarVisible = useAtom(navbarVisible)
			val root = useAtom(root)

			if (navbarVisible == false) return
			NavigationBar {
				navbarItems.forEach {
					NavigationBarItem(
						selected = root == it.root,
						onClick = { redirect(it.root) },
						label = {
							Text(Res.string(it.title))
						},
						icon = {
							BadgedBox(
								badge = {
									if (it.count > 0)
										Badge {
											Text(if (it.count > 99) "+99" else it.count.toString())
										}
								}
							) {
								Icon(
									imageVector = Res.drawable(it.icon),
									contentDescription = Res.string(it.title)
								)
							}
						}
					)
				}
			}
		}
	}
}
