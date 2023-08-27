package zustbase.basepage.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import ui.colorBlack
import zustbase.custom.pressClickEffect
import zustbase.services.models.ZustService
import zustbase.services.models.ZustServiceData

private const val DEFAULT_SERVICE_CHUNK_SIZE = 4
fun LazyListScope.zustAvailServicesUi(data: ZustServiceData?, callback: (ZustService) -> Unit) {
    if (data == null) {
        return
    }
    if (data.serviceList.isNullOrEmpty()) {
        return
    }
    val chunkedServices = data.serviceList.chunked(DEFAULT_SERVICE_CHUNK_SIZE)
    items(chunkedServices.size) { index ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dp_16, vertical = dp_8)
        ) {
            val chunk = chunkedServices[index]
            for (i in (0 until DEFAULT_SERVICE_CHUNK_SIZE)) {
                if (i < chunk.size) {
                    ZustSingleServiceUi(
                        chunk[i],
                        modifier = Modifier
                            .pressClickEffect {
                                callback.invoke(chunk[i])
                            }
                            .weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

        }
    }
}

@Composable
private fun ZustSingleServiceUi(zustService: ZustService, modifier: Modifier = Modifier) {
    if (zustService.imageUrl.isNullOrEmpty()){
        return
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = dp_4)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(zustService.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(dp_8))
        Text(
            text = zustService.title ?: "",
            style = ZustTypography.bodySmall,
            color = colorBlack,
        )
    }
}