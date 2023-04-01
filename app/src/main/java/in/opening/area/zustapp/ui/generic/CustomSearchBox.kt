package `in`.opening.area.zustapp.ui.generic

import `in`.opening.area.zustapp.R.color
import `in`.opening.area.zustapp.R.drawable
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

const val KEY_SEARCH = "search"
fun LazyListScope.customHomePageSearch(callback: () -> Unit) {
    item(key = KEY_SEARCH) {
        val text by rememberSaveable { mutableStateOf("Search “Grocery”") }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                callback.invoke()
            }
            .background(color = colorResource(id = color.white),
                shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp)) {
            Text(text = text, style = Typography_Montserrat.body2,
                color = colorResource(id = color.new_hint_color))
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(id = drawable.new_search_icon),
                contentDescription = "Search", tint = colorResource(id = color.new_material_primary))
        }
    }
}