package com.example.academicpulse.view.components.global

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.utils.logcat
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.Icon
import com.example.academicpulse.view.components.basic.Image
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view_model.Store

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthorsRow(
	authors: List<User>,
	appendCurrentUser: Boolean = false,
	showProfileOnClick: Boolean = false,
	onRemoveItem: ((User) -> Unit)? = null,
) {
	val (list, setList) = useState { authors }

	LaunchedEffect(authors) {
		val array = ArrayList<User>(authors.toMutableList())
		if (appendCurrentUser) array.add(0, Store.user.current.value!!)
		setList(array)
	}

	// Note: Use FlowRow instead of Row for the (overflow: break) option
	FlowRow(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(gap),
	) {
		list.forEach { author ->
			var modifier: Modifier = Modifier
			if (showProfileOnClick)
				modifier = modifier.clickable {
					if (author.id == Store.user.current.value!!.id)
						Router.navigate("profile/index", true)
					else
						logcat("User click: Show another user profile by id {${author.id}}")
				}
			Row(
				modifier = modifier,
				horizontalArrangement = Arrangement.spacedBy((gap.value / 3).dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				Image(id = R.drawable.avatar_user, size = 18.dp)
				Text(text = "${author.firstName} ${author.lastName}")
				if (onRemoveItem != null && author.id != Store.user.current.value!!.id) {
					Icon(id = R.drawable.icon_close, color = Color.Black.copy(alpha = 0.6f)) {
						onRemoveItem(author)
					}
				}
			}
		}
	}
}
