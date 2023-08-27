package zustbase.basepage.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppDeepLinkHandler
import zustbase.basepage.models.ServicePageSingleItemData
import zustbase.custom.pressClickEffect


fun LazyListScope.zustBaseRow2ItemsUi(list: List<ServicePageSingleItemData>) {

    item {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp_16, vertical = dp_8)
        ) {
            list.forEachIndexed { itemIndex, service ->
                val startPadding = if (itemIndex == 0) 0.dp else dp_8
                val endPadding = if (itemIndex == list.lastIndex) 0.dp else dp_8
                ZustBaseRow2SingleItemUi(service, modifier = Modifier
                    .pressClickEffect {
                        AppDeepLinkHandler.handleOfferLink(context, service.deepLink)
                    }
                    .weight(1f)
                    .padding(start = startPadding, end = endPadding))
            }
        }
    }
}

@Composable
private fun ZustBaseRow2SingleItemUi(data: ServicePageSingleItemData, modifier: Modifier = Modifier) {
    if (data.imageUrl.isNullOrEmpty()) {
        return
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(dp_4))
        )
    }
}