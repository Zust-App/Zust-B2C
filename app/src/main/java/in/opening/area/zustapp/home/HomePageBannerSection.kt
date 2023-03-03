package `in`.opening.area.zustapp.home

import `in`.opening.area.zustapp.home.models.HomePageGenericData
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
fun HomePageBannerSection(sectionList: List<Any>?) {
    if (sectionList.isNullOrEmpty()) {
        return
    }
    LazyRow(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)) {
        items(sectionList) {
            if (it is HomePageGenericData) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(145.dp)
                    )
                }
            }
        }
    }
}