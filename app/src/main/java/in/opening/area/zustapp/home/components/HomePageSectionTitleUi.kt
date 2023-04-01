package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.homePageSectionTitleUi(data: Any?) {
    if (data == null || data == "null") {
        return
    }
    if (data is String) {
        item() {
            Text(text = data,
                style = Typography_Montserrat.body1,
                modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp))
        }
    }
}