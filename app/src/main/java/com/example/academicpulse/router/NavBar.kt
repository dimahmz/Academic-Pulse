package com.example.academicpulse.router

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.theme.bottomBarHeight
import com.example.academicpulse.theme.descriptionTextSize
import com.example.academicpulse.theme.navBavIconColor
import com.example.academicpulse.theme.white
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view_model.Store

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
	NavbarItem("profile", R.string.profile, R.drawable.icon_profile),
)
private val visibleWhen = listOf("home", "notifications", "profile", "auth/activation")

/** Bottom NavBar UI element containing main root routes with their icons, allowing direct navigation to them */
@Composable
fun NavBar(route: String) {
	val liveUnReadNotificationsTotal by Store.notifications.unReadNotificationsTotal.collectAsState()
	if (!visibleWhen.contains(route)) return

	NavigationBar(
		modifier = Modifier.height(bottomBarHeight),
		containerColor = MaterialTheme.colorScheme.secondary,
	) {
		navbarItems.forEach {
			val selected = route == it.route
			val color = if (selected) MaterialTheme.colorScheme.primary else navBavIconColor
			NavigationBarItem(colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
				selected = selected,
				onClick = { if (!selected) Router.navigate(it.route) },
				label = { Text(text = it.title, color = color, size = descriptionTextSize) },
				icon = {
					BadgedBox(
						badge = {
							if (it.route == "notifications" && liveUnReadNotificationsTotal > 0) {
								Badge {
									Text(
										text = "$liveUnReadNotificationsTotal" , color= white
									)
								}
							}
						},
					) {
						Icon(id = it.icon, color = color, size = 20.dp)
					}
				})
		}
	}
}
