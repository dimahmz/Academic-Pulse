package com.example.academicpulse.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource

class Res {
	companion object {
		/** Converts string resource IDs, such as R.string.title_home, to literal strings. */
		@Composable
		fun string(id: Int): String {
			return stringResource(id)
		}

		/** Converts image/icon resource IDs, such as R.drawable.icon_home, to literal ImageVectors. */
		@Composable
		fun drawable(id: Int): ImageVector {
			return ImageVector.vectorResource(id)
		}
	}
}