package com.example.academicpulse.view.pages.publications

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.academicpulse.R
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.utils.context
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.H3
import com.example.academicpulse.view.components.basic.Image
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.AuthorsRow
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view_model.Store

@Composable
fun SelectAuthorsPage() {
	val form = Store.publications.form
	val search = Field.use(form = null)
	val (loading, setLoading) = useState { true }
	val selectedList = useAtom(form.authors, arrayListOf())
	val (list, setList) = useState { arrayListOf<User>() }

	LaunchedEffect(search.value.trim()) {
		setLoading(true)
		Store.users.search(
			search.value,
			selectedList,
		) { array ->
			try {
				setList(array)
				setLoading(false)
			} finally {
			}
		}
	}

	fun onRemoveUser(it: User) {
		val array = ArrayList<User>(selectedList.toMutableList())
		array.remove(it)
		form.authors.value = array
		val array2 = ArrayList<User>((list).toMutableList())
		array2.add(it)
		setList(array2)
	}

	fun addUser(it: User) {
		if (selectedList.size >= 5) {
			Toast.makeText(context, R.string.authors_length, Toast.LENGTH_LONG).show()
		} else {
			val array2 = ArrayList<User>((list).toMutableList())
			array2.remove(it)
			setList(array2)
			val array = ArrayList<User>(selectedList.toMutableList())
			array.add(it)
			form.authors.value = array
		}
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 10.dp, bottom = 6.dp),
			contentAlignment = Alignment.CenterStart
		) {
			H3(
				text = R.string.authors, modifier = Modifier.fillMaxWidth(), align = TextAlign.Center
			)

			Row(Modifier.fillMaxWidth()) {
				Spacer(Modifier.weight(1f))
				Text(text = R.string.continued) { Router.back("publications/add-publication") }
			}
		}
		Line(height = 1.dp)
		Spacer(Modifier.height(10.dp))
		AuthorsRow(authors = selectedList, appendCurrentUser = true, onRemoveItem = ::onRemoveUser)
		Input(
			field = search,
			icon = R.drawable.icon_search,
			placeholder = R.string.search,
			outlined = false,
		)
		Line(height = 1.dp)

		if (loading) {
			Spinner()
			return
		}

		LazyColumn(Modifier.weight(1f)) {
			items(list, key = { it.id }) {
				Column {
					Spacer(Modifier.height(7.dp))
					Row(
						Modifier
							.fillMaxWidth()
							.clickable { addUser(it) }) {
						if (it.photoUrl.toString().isEmpty()) {
							Image(id = R.drawable.avatar_user, size = 18.dp)
						} else {
							AsyncImage(
								model = it.photoUrl,
								contentDescription = null,
								modifier = Modifier
									.size(18.dp)
									.clip(CircleShape),
								contentScale = ContentScale.Crop
							)
						}
						Text(text = "${it.firstName} ${it.lastName}")
					}
					Spacer(Modifier.height(7.dp))
					Line(height = 1.dp)
				}
			}
		}
	}

	BackHandler { Router.back("publications/add-publication") }
}
