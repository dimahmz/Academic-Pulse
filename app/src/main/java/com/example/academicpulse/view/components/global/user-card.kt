package com.example.academicpulse.view.components.global

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view_model.Store
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserCard() {
	val user = useAtom(Store.users.current)
	var profileImageUri by remember { mutableStateOf<Uri?>(null) }
	val currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
	val (photoIsLoading, setPhotoIsLoading) = useState { false }


	val pickImageLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument()
	) { uri: Uri? ->

		uri?.let {
			profileImageUri = it
		}
	}

	LaunchedEffect(profileImageUri) {
		profileImageUri?.let { uri ->
			setPhotoIsLoading(true)
			Store.users.changeProfilePicture(uri)
			setPhotoIsLoading(false)
		}
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
				Router.navigate("profile/settings")
			}
		}
		
		if (user == null) {
			ErrorMessage()
			return@Column
		}
		Box {
			// photo is uploading
			if (photoIsLoading) {
				Spinner()
			}
			// there is no Photo url
			else if (currentUser?.photoUrl == null) {
				Image(id = R.drawable.avatar_user, size = 80.dp) {
					pickImageLauncher.launch(arrayOf("image/*"))
				}
			} else {
				// render the user image
				AsyncImage(
					model = currentUser?.photoUrl,
					contentDescription = null,
					modifier = Modifier
						.size(80.dp) // Set height and width
						.clip(CircleShape)
						.clickable {
							pickImageLauncher.launch(arrayOf("image/*"))
						},
					contentScale = ContentScale.Crop
				)
			}
		}

		H2(text = "${user.firstName} ${user.lastName}")

		if (!user.institutionSkipped) FlowRow(
			horizontalArrangement = Arrangement.spacedBy(gap),
		) {
			if (user.institution.isNotBlank()) {
				Box(modifier = Modifier.align(Alignment.CenterVertically)) {
					Icon(id = R.drawable.icon_dot, size = 7.dp)
				}
				Text(text = user.institution)
			}
			if (user.department.isNotBlank()) {
				Box(modifier = Modifier.align(Alignment.CenterVertically)) {
					Icon(id = R.drawable.icon_dot, size = 7.dp)
				}
				Text(text = user.department)
			}
			if (user.position.isNotBlank()) {
				Box(modifier = Modifier.align(Alignment.CenterVertically)) {
					Icon(id = R.drawable.icon_dot, size = 7.dp)
				}
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
			) { Router.navigate("publications/add-publication") }
		}
	}
}

