package zustElectronics.zeLanding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography

@Composable
fun ZeHomeSearchBarUi() {
    val text by rememberSaveable { mutableStateOf("Search “Grocery”") }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable {

        }
        .background(color = colorResource(id = R.color.white),
            shape = RoundedCornerShape(8.dp))
        .padding(horizontal = 12.dp, vertical = 12.dp)) {
        Text(text = text, style = ZustTypography.body2,
            color = colorResource(id = R.color.new_hint_color))
        Spacer(modifier = Modifier.weight(1f))
        Icon(painter = painterResource(id = R.drawable.new_search_icon),
            contentDescription = "Search", tint = colorResource(id = R.color.new_material_primary))
    }
}

