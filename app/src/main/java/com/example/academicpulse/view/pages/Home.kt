package com.example.academicpulse.view.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import com.example.academicpulse.utils.useEffect
import com.example.academicpulse.utils.useState

@Composable
fun Home() {
	val (text, printIt) = useState("")
	val (box1, checkOption1) = useState(true)
	val (box2, checkOption2) = useState<Boolean?>(null)
	val (box3, checkOption3) = useState(false)
	useEffect(listOf(box1, box2, box3)) {
		printIt("One of the boxes changed")
	}

	Column {
		Text("Checked: $text")
		Button(onClick = { checkOption1(true) }) {
			Text("Check Option 1 $box1")
		}
		Button(onClick = { checkOption2(null) }) {
			Text("Check Option 2 $box2")
		}
		Button(onClick = { checkOption3(true) }) {
			Text("Check Option 3 $box3")
		}
	}
}
