package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useField
import com.example.academicpulse.utils.useForm
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header

@Composable
fun SignUpInstitutionPage() {
	val form = useForm()
	val institution = useField(
		form = form,
		value = "",
		ifEmpty = "The institution is required",
	)
	val department = useField(
		form = form,
		value = "",
		ifEmpty = "The department is required",
	)
	val position = useField(
		form = form,
		value = "",
		ifEmpty = "The position is required",
	)

	fun validate () {
		if (form.validate()) {
			Router.navigate("auth/sign-up-user", false)
		} else form.focusOnFirstInvalidField()
	}

	Column(
		modifier = Modifier
			.padding(horizontal = pagePaddingX)
			.fillMaxHeight()
	) {
		Header(title = R.string.signup)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(text = R.string.institution_info)
			Description(text = R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Input(
				field = institution,
				label = Res.string(R.string.institution),
				focusNext = department.focusRequester,
			)
			Input(
				field = department,
				label = Res.string(R.string.department),
				focusNext = position.focusRequester,
			)
			Input(
				field = position,
				label = Res.string(R.string.position),
			)
			form.Error()
		}

		Spacer(Modifier.weight(1f)) // Apply flex-grow: 1 on a fake block to move the below buttons to the bottom of page.
		Column(
			modifier = Modifier.padding(bottom = 60.dp),
			verticalArrangement = Arrangement.spacedBy(gap)
		) {
			Button(text = R.string.next) { validate() }
			Button(text = R.string.skip, ghost = true) {
				Router.navigate("auth/sign-up-user", false)
			}
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpInstitutionPage() {
	SignUpInstitutionPage()
}
