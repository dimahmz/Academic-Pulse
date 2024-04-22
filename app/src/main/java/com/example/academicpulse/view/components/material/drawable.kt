package com.example.academicpulse.view.components.material

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.academicpulse.utils.Res

@Composable
fun Icon(id: Int, onClick: (() -> Unit)? = null) {
	val modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
	Icon(imageVector = Res.drawable(id), contentDescription = null, modifier = modifier)
}
