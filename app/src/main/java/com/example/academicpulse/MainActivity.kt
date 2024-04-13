package com.example.academicpulse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.academicpulse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val	appContainer = ActivityMainBinding.inflate(layoutInflater)
		setContentView(appContainer.root)

		val routesContainer = findNavController(R.id.binding_fragment)
		val routes = setOf(
			R.id.route_home,
			R.id.route_inbox,
			R.id.route_profile
		)
		setupActionBarWithNavController(routesContainer, AppBarConfiguration(routes))
		appContainer.navigationMenu.setupWithNavController(routesContainer)
	}
}