package com.example.academicpulse.model.DB

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.academicpulse.R

data class Author(
	val uid: String,
	val firstName: String,
	val lastName: String,
	private val image: ImageVector? = null,
) {
	val profile
		@Composable get() = image ?: ImageVector.vectorResource(R.drawable.avatar_user)
}

