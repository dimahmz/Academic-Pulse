package com.example.academicpulse.view.pages.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.model.Publication
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.view_model.Store


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun UserPublicationsTab() {

	var state by remember { mutableStateOf(0) }
	val titles = listOf(
		mapOf("label" to "All", "status" to ""),
		mapOf("label" to "Accepted", "status" to "accepted"),
		mapOf("label" to "Pending", "status" to "pending"),
		mapOf("label" to "Rejected", "status" to "rejected")
	)

	fun onTabSelected(index: Int, status: String) {
		state = index
		// when selects all the publications
		if (status == "") {
			Store.publications.userFilteredPublications.value = Store.publications.userPublications.value!!
			return
		}

		var filteredPublications =
			Store.publications.userPublications.value?.filter { it.status == status }
		if (filteredPublications == null) {
			Store.publications.userFilteredPublications.value = arrayListOf()
		} else {
			Store.publications.userFilteredPublications.value =
				(filteredPublications as ArrayList<Publication>?)!!
		}
	}
	Column {
		PrimaryTabRow(selectedTabIndex = state) {
			titles.forEachIndexed { index, title ->
				Tab(selected = state == index,
					onClick = { onTabSelected(index, title["status"]!!) },
					text = { Text(text = title["label"]!!, maxLines = 2, overflow = TextOverflow.Ellipsis) })
			}
		}
	}
}
