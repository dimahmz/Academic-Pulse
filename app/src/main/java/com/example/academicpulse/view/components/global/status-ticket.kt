package com.example.academicpulse.view.components.global


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.academicpulse.R
import com.example.academicpulse.theme.green
import com.example.academicpulse.theme.orange
import com.example.academicpulse.theme.red
import com.example.academicpulse.theme.white
import com.example.academicpulse.view.components.basic.Text

@Composable
fun StatusTicket(status: String = "rejected") {
	var color: Color = green
	var statusText: Int = R.string.accepted_status
	if (status.isNotEmpty() && status != "accepted") {
		if (status == "pending") {
			color = orange
			statusText = R.string.pending_status
		} else {
			color = red
			statusText = R.string.rejected_status
		}
		Row {

			Box(
				modifier = Modifier
					.wrapContentSize()
					.clip(RoundedCornerShape(6.dp))
					.background(color)
					.padding(horizontal = 13.dp, vertical = 6.dp), contentAlignment = Alignment.Center
			) {
				Text(text = statusText, color = white, size = 13.sp)
			}
			Spacer(Modifier.height(15.dp))
		}
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewStatusTicker() {
	StatusTicket()
}

