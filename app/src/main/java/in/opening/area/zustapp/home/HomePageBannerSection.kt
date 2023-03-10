package `in`.opening.area.zustapp.home

import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun HomePageBannerSection(bannerItem: Any?) {
    if (bannerItem == null) {
        return
    }
    val context = LocalContext.current
    if (bannerItem is HomePageGenericData) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(bannerItem.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .clickable {
                    AppDeepLinkHandler.handleOfferLink(context, bannerItem)
                }
        )
    }
}
