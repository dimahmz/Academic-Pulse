package com.example.academicpulse.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.academicpulse.R

data class User(
	val id: String,
	val name: String,
	private val image: ImageVector? = null,
) {
	val profile
		@Composable
		get() = image ?: ImageVector.vectorResource(R.drawable.avatar_user)
}
