package com.example.academicpulse

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.academicpulse.theme.AppTheme
import com.example.academicpulse.utils.saveAppContext
import com.example.academicpulse.router.Router
import com.example.academicpulse.view_model.Store

class Index : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		saveAppContext(this)
		installSplashScreen().setKeepOnScreenCondition { !Store.isReady() }
		setContent {
			App()
		}
	}
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {
	val startDestination = "auth"

	Router.Provider()
	AppTheme {
		Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
			Scaffold(
				bottomBar = {
					Router.NavBar(startDestination)
				}
			) {
				Router.NavGraph(startDestination)
			}
		}
	}
}
