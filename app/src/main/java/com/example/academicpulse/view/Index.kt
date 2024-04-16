package com.example.academicpulse.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.example.academicpulse.theme.AppTheme
import com.example.academicpulse.utils.saveAppContext
import com.example.academicpulse.view.layout_components.Router
import com.example.academicpulse.view.pages.Home

class Index : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		saveAppContext(this)
		Router.init()
		setContent {
			App()
		}
	}
}

@Composable
fun App() {
	AppTheme {
		Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
			Scaffold(
				bottomBar = {
					Router.NavBar()
				}
			) {
				Home()
			}
		}
	}
}
