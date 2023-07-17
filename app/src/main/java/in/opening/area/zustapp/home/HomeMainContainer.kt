package `in`.opening.area.zustapp.home

import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.home.components.HomePageErrorUi
import `in`.opening.area.zustapp.home.composeContainer.HomePageShimmerUi
import `in`.opening.area.zustapp.uiModels.HomePageResUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.HomeViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun HomeMainContainer(homeViewModel: HomeViewModel,
                      paddingValues: PaddingValues, callback: (ACTION) -> Unit,changeLocationCallback:()->Unit) {
    val homeWidgets by homeViewModel.homePageUiState.collectAsState(HomePageResUi.InitialUi(false))
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(paddingValues)) {
        val (searchField, notDeliverHere, pgBar) = createRefs()

        val response = homeWidgets
        if (response.isLoading) {
            HomePageShimmerUi()
            CustomAnimatedProgressBar(modifier = Modifier.constrainAs(pgBar) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }
        when (response) {
            is HomePageResUi.HomeSuccess -> {
                HomeMainItemContainer(response.homePageGrids?.homeGrids,
                    response.trendingProducts?.productItems,
                    this, callback) { product, action ->
                    product?.itemCountByUser?.let { cartItemCount ->
                        if (product.maxItemPurchaseLimit > 0 && action == `in`.opening.area.zustapp.viewmodels.ACTION.INCREASE) {
                            if (cartItemCount <= product.maxItemPurchaseLimit) {
                                homeViewModel.updateOrInsertItems(product, action)
                                return@let
                            } else {
                                AppUtility.showToast(context, "You can't add more than ${product.maxItemPurchaseLimit}")
                            }
                        } else {
                            homeViewModel.updateOrInsertItems(product, action)
                        }
                    }
                }
            }

            is HomePageResUi.ErrorUi -> {
                if (!response.errorMsg.isNullOrEmpty()) {
                    AppUtility.showToast(context, response.errorMsg)
                } else {
                    AppUtility.showToast(context, response.errors.getTextMsg())
                }
                HomePageErrorUi(response.errorCode, searchField, notDeliverHere, this, {
                    homeViewModel.getUserSavedAddress()
                }) {
                    changeLocationCallback.invoke()
                }
            }

            is HomePageResUi.InitialUi -> {
                if (response.isLoading) {

                }
            }
        }
    }
}
