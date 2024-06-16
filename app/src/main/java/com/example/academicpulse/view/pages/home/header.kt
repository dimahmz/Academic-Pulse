package com.example.academicpulse.view.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view_model.Store

@Composable
fun HomeHeader() {
	val search = Field.use(form = null)

	LaunchedEffect(search.value.trim()) {
		Store.publications.search(search.value) {
			Store.publications.filtredHomePublications.value = it
		}
	}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.primary)
			.padding(vertical = 25.dp, horizontal = 15.dp),
	) {
		Input(
			field = search,
			icon = R.drawable.icon_search,
			placeholder = R.string.search,
		)
	}
}
