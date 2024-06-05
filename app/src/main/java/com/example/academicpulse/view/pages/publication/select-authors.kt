package com.example.academicpulse.view.pages.publication

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.model.User
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.context
import com.example.academicpulse.utils.useAtom
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.H3
import com.example.academicpulse.view.components.basic.Image
import com.example.academicpulse.view.components.basic.Input
import com.example.academicpulse.view.components.basic.Spinner
import com.example.academicpulse.view.components.basic.Text
import com.example.academicpulse.view.components.global.AuthorsRow
import com.example.academicpulse.view.components.global.Line
import com.example.academicpulse.view_model.Store
import com.example.academicpulse.view_model.StoreDB

@Composable
fun SelectAuthorsPage() {
	val search = useField(form = useForm())
	val (loading, setLoading) = useState { true }
	val selectedList = useAtom(Store.publications.currentFormAuthors, arrayListOf())
	val (list, setList) = useState { arrayListOf<User>() }

	LaunchedEffect(search.value.trim()) {
		setLoading(true)
		StoreDB.getAll(
			collection = "user",
			{ setLoading(false) },
			{ id, data -> User.fromMap(id, data) },
			{ array ->
				setList(array)
				setLoading(false)
			}
		)
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
				text = R.string.authors,
				modifier = Modifier.fillMaxWidth(),
				align = TextAlign.Center
			)

			Row(Modifier.fillMaxWidth()) {
				Spacer(Modifier.weight(1f))
				Text(text = R.string.continued) { Router.back(false) }
			}
		}
		Line(height = 1.dp)
		Spacer(Modifier.height(10.dp))
		AuthorsRow(authors = selectedList, appendCurrentUser = true) {
			// At click on X icon: unselect the user and add it to the non-selected ones
			val array = ArrayList<User>(selectedList.toMutableList())
			array.remove(it)
			Store.publications.currentFormAuthors.value = array
			val array2 = ArrayList<User>((list).toMutableList())
			array2.add(it)
			setList(array2)
		}
		Input(
			field = search,
			icon = R.drawable.icon_search,
			placeholder = R.string.search,
			outlined = false,
		)
		Line(height = 1.dp)

		LazyColumn(Modifier.weight(1f)) {
			if (loading) {
				item(key = "Page header") {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 30.dp),
						contentAlignment = Alignment.Center
					) {
						Spinner(size = 30.dp)
					}
				}
			} else {
				items(list, key = { it.id }) {
					Column {
						Spacer(Modifier.height(7.dp))
						Row(
							Modifier
								.fillMaxWidth()
								.clickable {
									if (selectedList.size >= 5) {
										Toast
											.makeText(context, R.string.authors_length, Toast.LENGTH_LONG)
											.show()
									} else {
										val array2 = ArrayList<User>((list).toMutableList())
										array2.remove(it)
										setList(array2)
										val array = ArrayList<User>(selectedList.toMutableList())
										array.add(it)
										Store.publications.currentFormAuthors.value = array
									}
								}) {
							Image(id = R.drawable.avatar_user, size = 18.dp)
							Text(text = "${it.firstName} ${it.lastName}")
						}
						Spacer(Modifier.height(7.dp))
						Line(height = 1.dp)
					}
				}
			}
		}
	}

	BackHandler { Router.back(false) }
}
