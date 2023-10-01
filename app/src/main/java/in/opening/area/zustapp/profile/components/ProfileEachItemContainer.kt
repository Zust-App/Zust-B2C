package `in`.opening.area.zustapp.profile.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.models.ProfileStaticItem
import `in`.opening.area.zustapp.ui.theme.zustFont
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileEachItemContainer(profileStaticItem: ProfileStaticItem, clickEvent: (Int?) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(profilePaddingValues)
        .clickable {
            clickEvent.invoke(profileStaticItem.eventCode)
        }) {
        Icon(painter = painterResource(id = profileStaticItem.iconRes?:R.drawable.baseline_image_24),
            contentDescription = "",
            modifier = Modifier
                .padding(start = 8.dp, top = 12.dp)
                .size(30.dp)
                .background(color = colorResource(id = R.color.screen_surface_color),
                    shape = RoundedCornerShape(8.dp))
                .padding(4.dp)
                .align(Alignment.CenterVertically),
            tint = colorResource(id = R.color.light_black))

        Text(text = AnnotatedString(text = profileStaticItem.text),
            modifier = Modifier.padding(start = 8.dp,
                end = 16.dp, top = 16.dp), style = TextStyle(
                color = colorResource(id = R.color.black_3),
                fontSize = 14.sp,
                fontFamily = zustFont,
                fontWeight = FontWeight.W500
            ))
        Spacer(modifier = Modifier.weight(1.0f))
        Icon(painter = painterResource(R.drawable.ic_baseline_navigate_next_24),
            contentDescription = "",
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .align(Alignment.CenterVertically),
            tint = colorResource(id = R.color.light_black))
    }
}