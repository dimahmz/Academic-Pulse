package com.example.academicpulse.view.components.global

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.view.components.basic.*

@Composable
fun ProfileCard(onAddResearch: () -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = pagePaddingX),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Row(modifier = Modifier.padding(top = pagePaddingX)) {
			Spacer(Modifier.weight(1f))
			Icon(id = R.drawable.icon_settings, size = 20.dp) {
				Router.navigate("profile/settings", false)
			}
		}

		Image(id = R.drawable.avatar_user, size = 80.dp)
		Title(text = "User name")

		Row(
			modifier = Modifier.padding(top = 4.dp),
			horizontalArrangement = Arrangement.spacedBy(gap)
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(text = "Degree")
				Icon(id = R.drawable.icon_dot, size = 7.dp)
			}
			Row(
				horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(text = "Position")
				Icon(id = R.drawable.icon_dot, size = 7.dp)
			}
			Text(text = "Degree")
		}

		Row(
			modifier = Modifier.padding(top = 26.dp, bottom = 14.dp),
			horizontalArrangement = Arrangement.spacedBy(gap),
		) {
			Button(
				text = R.string.add_research,
				icon = R.drawable.icon_add,
				modifier = Modifier.weight(1f),
				height = 40.dp,
				onClick = onAddResearch
			)
			Button(
				text = R.string.edit_profile,
				icon = R.drawable.icon_edit,
				modifier = Modifier.weight(1f),
				height = 40.dp,
				ghost = true,
			) {
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileCard() {
	ProfileCard { /* At click on add research. */ }
}
