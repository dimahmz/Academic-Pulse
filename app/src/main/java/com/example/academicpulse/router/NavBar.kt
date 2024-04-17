package com.example.academicpulse.router

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.academicpulse.R
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState

data class NavbarItem(
	val route: String,
	val title: Int,
	val icon: Int,
	var count: Int = 0,
)

val navbarItems = listOf(
	NavbarItem("home", R.string.title_home, R.drawable.icon_home),
	NavbarItem("inbox", R.string.title_inbox, R.drawable.icon_inbox),
	NavbarItem("profile", R.string.title_profile, R.drawable.icon_profile),
)

@Composable
fun NavBar() {
	val navBarVisible = useAtom(Router.isNavBarVisible())
	val route = useAtom(Router.getRoute())

	if (navBarVisible != true) return
	NavigationBar {
		navbarItems.forEach {
			NavigationBarItem(
				selected = it.route.startsWith(route ?: "home"),
				onClick = { Router.navigate(it.route, true) },
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