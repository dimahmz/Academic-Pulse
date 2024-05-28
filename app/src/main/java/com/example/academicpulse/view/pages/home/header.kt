package com.example.academicpulse.view.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.view.components.basic.Input

@Composable
fun HomeHeader() {
	val form = useForm()
	val search = useField(form = form)
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.primary)
			.padding(vertical = 25.dp, horizontal = 15.dp),
	) {
		Input(field = search, icon = R.drawable.icon_search)
	}
}
