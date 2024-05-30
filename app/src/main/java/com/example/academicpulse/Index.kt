package com.example.academicpulse

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.academicpulse.theme.AppTheme
import com.example.academicpulse.router.Router
import com.example.academicpulse.utils.context
import com.example.academicpulse.view_model.Store
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings

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
				Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
					Scaffold(
						content = { Router.NavGraph() },
						bottomBar = { Router.NavBar() },
					)
				}
			}
		}
	}

	private fun configureFirebaseServices() {
		if (DEV_ENV) {
			Firebase.auth.useEmulator(LOCALHOST, AUTH_PORT)
			Firebase.firestore.useEmulator(LOCALHOST, FIRESTORE_PORT)
			Firebase.firestore.firestoreSettings = firestoreSettings {
				isPersistenceEnabled = false
			}
		}
	}
}
