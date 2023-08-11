package zustbase.basepage.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
import zustbase.custom.pressClickEffect
import zustbase.services.models.ZustService
import zustbase.services.models.ZustServiceData

private const val DEFAULT_SERVICE_CHUNK_SIZE = 3
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
            chunk.forEachIndexed { itemIndex, service ->
                val startPadding = if (itemIndex == 0) 0.dp else dp_8
                val endPadding = if (itemIndex == chunk.lastIndex) 0.dp else dp_8
                ZustSingleServiceUi(
                    service,
                    modifier = Modifier
                        .pressClickEffect {
                            callback.invoke(service)
                        }
                        .weight(1f)
                        .padding(start = startPadding, end = endPadding)
                )
            }
            if (chunk.size < (DEFAULT_SERVICE_CHUNK_SIZE)) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ZustSingleServiceUi(zustService: ZustService, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(zustService.imageUrl ?: "https://ik.imagekit.io/dunzo/home/tr:w-488,h-360_home_icon/operator-FFWUCfzmUzhok89HMYt0ON2Gy5oZECO73gRenPw11HxAeCLBtTBOG8FMqMTe92UOnScOPMUnjYDcaPVxx7wSFJwXJ3kSR3YRsPby4EgC4zW2mVYLc99zuvVh7O2Ppmx2QMQd40UiwYLGhy0OjbMayr.png")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .height(110.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(dp_4))
        )
        Spacer(modifier = Modifier.height(dp_8))
        Text(
            text = zustService.title ?: "",
            style = ZustTypography.body2,
            color = colorResource(id = R.color.black),
        )
    }
}