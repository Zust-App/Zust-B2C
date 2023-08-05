package non_veg.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.dp_10
import non_veg.home.viewmodel.ZustNvEntryViewModel

@Composable
fun ZNonVegHomeBrandingUi(viewModel: ZustNvEntryViewModel) {
    Box(modifier = Modifier
        .shadow(elevation = dp_10,
            spotColor = Color(0x1A000000),
            ambientColor = Color(0x1A000000))
        .fillMaxWidth()
        .height(200.dp)) {
        Image(
            painter = painterResource(id = R.drawable.z_non_veg_branding_icon),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds
        )
    }
}