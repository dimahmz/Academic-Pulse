package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.material.*
import com.example.academicpulse.view_model.AuthViewModel

@Composable
fun SignUpInstitutionPage(viewModel: AuthViewModel) {
	val (institution, setInstitution) = useState("")
	val (institutionValid, setInstitutionValidity) = useState(true)
	val (department, setDepartment) = useState("")
	val (departmentValid, setDepartmentValidity) = useState(true)
	val (position, setPosition) = useState("")
	val (positionValid, setPositionValidity) = useState(true)

	Column(
		modifier = Modifier
			.padding(horizontal = pagePaddingX)
			.fillMaxHeight()
	) {
		Header(title = R.string.signup, arrow = false)

		Column(modifier = Modifier.padding(bottom = 30.dp)) {
			Title(R.string.institution_info)
			Description(R.string.institution_info_description)
		}

		Column(verticalArrangement = Arrangement.spacedBy(gap)) {
			val (departmentFocus) = useState(FocusRequester())
			val (positionFocus) = useState(FocusRequester())

			Input(
				value = institution,
				onChange = setInstitution,
				label = Res.string(R.string.institution),
				required = true,
				valid = institutionValid,
				onChangeValidity = setInstitutionValidity,
				focusNext = departmentFocus,
			)
			Input(
				value = department,
				onChange = setDepartment,
				label = Res.string(R.string.department),
				required = true,
				valid = departmentValid,
				onChangeValidity = setDepartmentValidity,
				focusRequester = departmentFocus,
				focusNext = positionFocus,
			)
			Input(
				value = position,
				onChange = setPosition,
				label = Res.string(R.string.position),
				required = true,
				valid = positionValid,
				onChangeValidity = setPositionValidity,
				focusRequester = positionFocus,
			)
		}

		Spacer(Modifier.weight(1f)) // Apply flex-grow: 1 on a fake block to move the below buttons to the bottom of page.

		Column(modifier = Modifier.padding(bottom = 60.dp), verticalArrangement = Arrangement.spacedBy(gap)) {
			Button(text = R.string.next) {
				Router.navigate("auth/sign-up-user", false)
			}
			Button(text = R.string.skip, ghost = true) {
				Router.navigate("auth/sign-up-user", false)
			}
		}
	}
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSignUpInstitutionPage() {
	SignUpInstitutionPage(AuthViewModel())
}
