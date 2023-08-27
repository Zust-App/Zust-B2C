package non_veg.listing.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.NoProductFoundErrorPage
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.navigateToNonVegProductDetails
import non_veg.listing.uiModel.NonVegProductListingUiModel
import non_veg.listing.viewmodel.NonVegListingViewModel

private val spacerHeight20 = Modifier.height(dp_20)
private val spacerHeight12 = Modifier.height(dp_12)

@Composable
fun NonVegListingMainContainer(paddingValues: PaddingValues, viewModel: NonVegListingViewModel, suggestProductCallback: () -> Unit) {
    val context = LocalContext.current
    val nonVegProductListUiModel = viewModel.nonVegProductListUiModel.collectAsState().value
    ConstraintLayout(modifier = Modifier
        .padding(paddingValues = paddingValues)
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = colorResource(R.color.screen_surface_color))) {
        if (nonVegProductListUiModel.isLoading) {
            NonVegListingFullScreenShimmer()
        }
        val (spacer, title, itemList) = createRefs()
        Spacer(modifier = Modifier.constrainAs(spacer) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
        when (nonVegProductListUiModel) {
            is NonVegProductListingUiModel.Success -> {
                val productList = nonVegProductListUiModel.data
                if (!productList.isNullOrEmpty()) {
                    Text(text = "Showing result of ${productList.size} Items",
                        style = ZustTypography.bodyMedium,
                        modifier = Modifier
                            .padding(top = 16.dp,
                                start = 20.dp, end = 20.dp)
                            .constrainAs(title) {
                                top.linkTo(spacer.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            })
                    LazyColumn(modifier = Modifier
                        .padding(start = dp_20, end = dp_20,
                            top = dp_20)
                        .fillMaxWidth()
                        .constrainAs(itemList) {
                            top.linkTo(title.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        }) {
                        items(productList) { singleItem ->
                            NonVegListingItemUiV2(singleItem, viewModel)
                            Spacer(modifier = spacerHeight12)
                        }
                        item {
                            Spacer(modifier = Modifier.height(dp_20))
                        }
                        item {
                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "That's all my folks",
                                    color = colorResource(id = R.color.new_hint_color),
                                    style = ZustTypography.bodyMedium)
                            }
                        }
                    }
                } else {
                    NoProductFoundErrorPage(layoutScope = this, topReference = spacer) {
                        suggestProductCallback.invoke()
                    }
                }
            }

            is NonVegProductListingUiModel.Empty -> {
                val message = nonVegProductListUiModel
            }

            is NonVegProductListingUiModel.Error -> {
                val errorMessage = nonVegProductListUiModel.errorMessage
                AppUtility.showToast(context, errorMessage)
            }
        }
    }
}

@Composable
private fun NonVegListingFullScreenShimmer() {
    val shimmerColors = listOf(
        Color(0xffDBDBDB).copy(alpha = 0.8f),
        Color(0xffDBDBDB).copy(alpha = 0.5f),
        Color(0xffDBDBDB).copy(alpha = 1f),
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    NonVegListingFullScreenShimmerItem(brush = brush)
}

@Composable
private fun NonVegListingFullScreenShimmerItem(brush: Brush) {
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = 16.dp, vertical = 16.dp)) {
        items(count = 5) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Spacer(
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(brush)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
