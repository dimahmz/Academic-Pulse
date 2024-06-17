package com.example.academicpulse.router

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.descriptionTextSize
import com.example.academicpulse.theme.navBavIconColor
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Text

/** A helper class used below to render the NavBar. */
private data class NavbarItem(
	val route: String,
	val title: Int,
	val icon: Int,
	var count: Int = 0,
)

private val navbarItems = listOf(
	NavbarItem("home", R.string.home, R.drawable.icon_home),
	NavbarItem("notifications", R.string.notification, R.drawable.icon_notifications),
	NavbarItem("profile/index", R.string.profile, R.drawable.icon_profile),
)

/** Bottom NavBar UI element containing main root routes with their icons, allowing direct navigation to them */
@Composable
fun NavBar(route: String, navBarVisible: Boolean) {
	if (!navBarVisible) return
	NavigationBar(
		modifier = Modifier.height(bottomBarHeight),
		containerColor = MaterialTheme.colorScheme.secondary,
	) {
		navbarItems.forEach {
			val selected = route.startsWith(it.route)
			val color = if (selected) MaterialTheme.colorScheme.primary else navBavIconColor
			NavigationBarItem(
				colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
				selected = selected,
				onClick = { Router.navigate(it.route, true) },
				label = { Text(text = it.title, color = color, size = descriptionTextSize) },
				icon = {
					BadgedBox(
						badge = {
							if (it.count > 0)
								Badge {
									Text(text = if (it.count > 99) "+99" else it.count.toString())
								}
						},
					) {
						Icon(id = it.icon, color = color, size = 20.dp)
					}
				}
			)
		}
	}
}
