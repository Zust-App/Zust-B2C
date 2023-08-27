package `in`.opening.area.zustapp.ui.generic

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomProgressBar() {
    CircularProgressIndicator(
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
    )
}