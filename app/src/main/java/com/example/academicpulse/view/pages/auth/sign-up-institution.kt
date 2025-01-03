package com.example.academicpulse.view.pages.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.forms.*
import com.example.academicpulse.view.components.basic.*
import com.example.academicpulse.view.components.global.Header
import com.example.academicpulse.view_model.Store

@Composable
fun SignUpInstitutionPage() {
	val auth = Store.auth
	val form = Form.use()
	val institution = Field.use(
		form = form,
		value = auth.signUpInfo.institution,
		ifEmpty = R.string.institution_required,
	)
	val department = Field.use(
		form = form,
		value = auth.signUpInfo.department,
		ifEmpty = R.string.department_required,
	)
	val position = Field.use(
		form = form,
		value = auth.signUpInfo.position,
		ifEmpty = R.string.position_required,
	)

	fun back() {
		auth.clearSignUp()
		Router.back("auth/sign-in")
	}

	fun navigate(validate: Boolean) {
		if (!validate || form.validate()) {
			auth.saveInstitutionInfo(institution.trim(), department.trim(), position.trim())
			Router.navigate("auth/sign-up")
		}
	}

	Column(
		modifier = Modifier
			.fillMaxHeight()
			.padding(horizontal = pagePaddingX),
	) {
		Header(title = R.string.sign_up, onClick = ::back)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(text = R.string.institution_info)
			Description(text = R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			Input(
				field = institution,
				label = R.string.institution,
				focusNext = department,
			)
			Input(
				field = department,
				label = R.string.department,
				focusNext = position,
			)
			Input(
				field = position,
				label = R.string.position,
				okIcon = ImeAction.Next,
				onOk = { navigate(validate = true) },
			)
			form.Error()
		}

		Spacer(Modifier.weight(1f)) // Apply flex-grow: 1 on a fake block to move the below buttons to the bottom of page.
		Column(
			modifier = Modifier.padding(bottom = 60.dp),
			verticalArrangement = Arrangement.spacedBy(gap)
		) {
			Button(text = R.string.next) { navigate(validate = true) }
			Button(text = R.string.skip, ghost = true) { navigate(validate = false) }
		}
	}

	BackHandler(onBack = ::back)
}
