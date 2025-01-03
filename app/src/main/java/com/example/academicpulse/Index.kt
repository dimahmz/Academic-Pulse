package com.example.academicpulse

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.academicpulse.theme.AppTheme
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.context
import com.example.academicpulse.view.components.global.ServerErrorAlertDialog
import com.example.academicpulse.view_model.Store
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage

class Index : ComponentActivity() {
	@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		context = this
		configureFirebaseServices()
		installSplashScreen().setKeepOnScreenCondition { !Store.isReady }
		setContent {
			Router.Provider()
			Store.Provider()
			AppTheme {
				Box {
					Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
						if (Store.applicationState.hasServerErrorOccurred.collectAsState().value) {
							ServerErrorAlertDialog {
								Store.applicationState.hasServerErrorOccurred.value = false
							}
						}
						Scaffold(
							content = { Router.NavGraph() },
							bottomBar = { Router.NavBar() },
						)
					}
				}
			}
		}
	}

	private fun configureFirebaseServices() {
		if (DEV_ENV) {
			Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
			Firebase.firestore.useEmulator(LOCALHOST, FIRE_STORE_PORT)
			Firebase.storage.useEmulator(LOCALHOST, STORAGE_PORT)
			Firebase.database.useEmulator(LOCALHOST, REALTIME_DB_PORT)
		}
	}
}
