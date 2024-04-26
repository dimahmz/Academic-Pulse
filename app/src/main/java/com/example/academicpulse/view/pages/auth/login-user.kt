package com.example.academicpulse.view.pages.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.academicpulse.R
import com.example.academicpulse.router.Router
import com.example.academicpulse.theme.gap
import com.example.academicpulse.theme.pagePaddingX
import com.example.academicpulse.utils.Res
import com.example.academicpulse.utils.useState
import com.example.academicpulse.view.components.basic.*


@Composable
fun LogInUserPage() {
    val (email, setEmail) = useState("")
    val (emailValid, setEmailValidity) = useState(true)
    val (password, setPassword) = useState("")
    val (passwordValid, setPasswordValidity) = useState(true)

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center,
        ) {
            Image(id = R.drawable.app_logo, modifier = Modifier.size(120.dp))
        }
        Column(
            modifier = Modifier
                .padding(horizontal = pagePaddingX)
                .fillMaxHeight()
        ) {
            H1(
                modifier = Modifier
                    .padding(vertical = 30.dp), text = R.string.welcome
            )
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                val (emailFocus) = useState(FocusRequester())
                val (passwordFocus) = useState(FocusRequester())

                Input(
                    value = email,
                    onChange = setEmail,
                    label = Res.string(R.string.email_address),
                    valid = emailValid,
                    onChangeValidity = setEmailValidity,
                    focusRequester = emailFocus,
                    focusNext = passwordFocus,
                )
                Input(
                    value = password,
                    onChange = setPassword,
                    label = Res.string(R.string.password),
                    placeholder = Res.string(R.string.create_password),
                    password = true,
                    valid = passwordValid,
                    onChangeValidity = setPasswordValidity,
                    focusRequester = passwordFocus,
                )
            }
            Spacer(Modifier.padding(vertical = 20.dp))
            Button(text = R.string.login) {
                Router.navigate("auth/confirm-email", false)
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 40.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                Text(text = R.string.new_member, modifier = Modifier.padding(end = 10.dp))
                Text(
                    text = R.string.register_now,
                    color = MaterialTheme.colorScheme.primary,
                    weight = FontWeight(700),

                    )

            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewLogInUserPage() {
    LogInUserPage()
}
