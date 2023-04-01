package `in`.opening.area.zustapp.home

import `in`.opening.area.zustapp.R.string

import `in`.opening.area.zustapp.home.components.*
import `in`.opening.area.zustapp.home.models.HomeGrid
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.ui.generic.customHomePageSearch
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension

@Composable
fun HomeMainItemContainer(
    homeItems: List<HomeGrid>?,
    trendingItemList: List<ProductSingleItem>?,
    layoutScope: ConstraintLayoutScope,
    callback: (ACTION) -> Unit,
    productItemClick: (ProductSingleItem?, `in`.opening.area.zustapp.viewmodels.ACTION) -> Unit,
) {
    val trendingItems = stringResource(string.trending_items)
    if (homeItems != null) {
        layoutScope.apply {
            val (homeMainList) = createRefs()
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(homeMainList) {
                    height = Dimension.fillToConstraints
                    top.linkTo(parent.top, dp_8)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {

                homeItems.forEach { homeData ->
                    if (homeData.type == "BANNER") {
                        customHomePageSearch {
                            callback.invoke(ACTION.SEARCH_PRODUCT)
                        }
                    }
                    if (homeData.type == "DELIVERY_ALERT") {
                        if (!homeData.data.isNullOrEmpty()) {
                            homePageDeliveryAlert(homeData.data) {
                                callback.invoke(it)
                            }
                        }
                    }
                    if (homeData.type == "BANNER") {
                        homePageBrandTagUi()
                        homePageSectionTitleUi(homeData.title)
                        if (!homeData.data.isNullOrEmpty()) {
                            customAutoScrollImageUi(homeData.data)
                        }
                    }
                    if (homeData.type == "CATEGORY") {
                        homePageSectionTitleUi(homeData.title)
                        categoryHolder(homeData.data)
                    }
                }
                if (!trendingItemList.isNullOrEmpty()) {
                    homePageSectionTitleUi(trendingItems)
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    trendingProductsUi(trendingItemList) { product, action ->
                        productItemClick.invoke(product, action)
                    }
                }
                orderViaWhatsappUi(callback)
                homeSuggestProductUi(callback)
                homePageBrandPromiseUi()
            }
        }
    }
}



