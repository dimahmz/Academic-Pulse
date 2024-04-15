package com.example.academicpulse.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.academicpulse.theme.AppTheme
import com.example.academicpulse.view.components.Greeting

class Index : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			App {
				Greeting(name = "Internal Pointer Variable")
			}
		}
	}
}

@Composable
fun App(content: @Composable () -> Unit) {
	AppTheme {
		Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
			content()
		}
	}
}
