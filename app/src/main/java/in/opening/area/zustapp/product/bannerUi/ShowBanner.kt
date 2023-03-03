package `in`.opening.area.zustapp.product.bannerUi

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
 fun ShowBanner(bannerUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(bannerUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .height(150.dp).width(IntrinsicSize.Max)
            .padding(start = 6.dp, end = 6.dp, bottom = 8.dp, top = 8.dp)
            .clip(MaterialTheme.shapes.small)
    )
}