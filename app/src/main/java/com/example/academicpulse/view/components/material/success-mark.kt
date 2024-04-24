import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.academicpulse.R
import androidx.compose.ui.Alignment

@Composable
fun SuccessMark(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon_success_circle), // Load circle drawable
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewSuccessMark() {
    SuccessMark()
}