package com.example.academicpulse.view.components.global

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store

@Composable
fun UserCard(onAddResearch: () -> Unit) {
	val (loading, setLoading) = useState { false }
	val user = useAtom(Store.user.current)

	LaunchedEffect(true) {
		setLoading(true)
		Store.user.getCurrentUser({}) { _, _ -> setLoading(false) }
	}

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = pagePaddingX),
		verticalArrangement = Arrangement.spacedBy(4.dp),
	) {
		Row(modifier = Modifier.padding(top = pagePaddingX)) {
			Spacer(Modifier.weight(1f))
			Icon(id = R.drawable.icon_settings, size = 20.dp) {
				Router.navigate("profile/settings", false)
			}
		}

		if (loading) Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 30.dp), contentAlignment = Alignment.Center
		) {
			Spinner(size = 30.dp)
		}
		else if (user != null) {
			Image(id = R.drawable.avatar_user, size = 80.dp)
			H2(text = "${user.firstName} ${user.lastName}")

			if (user.institution != null) {
				Row(
					horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
					verticalAlignment = Alignment.CenterVertically,
				) {
					Icon(id = R.drawable.icon_dot, size = 7.dp)
					Text(text = user.institution)
				}
			}

			if (user.department != null) {
				Row(
					horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
					verticalAlignment = Alignment.CenterVertically,
				) {
					Icon(id = R.drawable.icon_dot, size = 7.dp)
					Text(text = user.department)
				}
			}

			if (user.position != null) {
				Row(
					horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
					verticalAlignment = Alignment.CenterVertically,
				) {
					Icon(id = R.drawable.icon_dot, size = 7.dp)
					Text(text = user.position)
				}
			}

			Row(
				modifier = Modifier.padding(top = 26.dp, bottom = 14.dp),
				horizontalArrangement = Arrangement.spacedBy(gap),
			) {
				Button(
					text = R.string.add_research,
					icon = R.drawable.icon_add,
					modifier = Modifier.weight(1f),
					onClick = onAddResearch
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewUserCard() {
	UserCard { /* At click on add research. */ }
}

