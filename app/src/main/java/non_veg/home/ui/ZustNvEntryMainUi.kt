package non_veg.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.components.homePageBrandPromiseUi
import `in`.opening.area.zustapp.home.composeContainer.HomePageShimmerUi
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_32
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.ui.theme.zustTypographySecondary
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.home.uiModel.NvHomePageCombinedUiModel
import non_veg.home.viewmodel.ZustNvEntryViewModel

private const val NV_HOME_OFFER = 1
private const val NV_HOME_BANNER = 2

@Composable
fun ZustNvEntryMainUi(viewModel: ZustNvEntryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), paddingValues: PaddingValues, searchCallback: () -> Unit) {
    val nvHomePageCombinedData = viewModel.nonVegHomePageUiModel.collectAsState().value

    if (nvHomePageCombinedData.isLoading) {
        HomePageShimmerUi()
        ShowNonVegProgressBar()
    }

    when (nvHomePageCombinedData) {
        is NvHomePageCombinedUiModel.Success -> {
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues = paddingValues)
                .background(color = colorResource(id = R.color.white))) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {

                    homeGenericPageSearchDefaultUi(arrayListOf("Search `Eggs`", "Search `Meat`", "Search `Chicken`")) {
                        searchCallback.invoke()
                    }

                    nvHomePageCombinedData.data?.homeBanner?.let {
                        item {
                            Spacer(modifier = Modifier.height(dp_20))
                        }
                        item(NV_HOME_BANNER) {
                            ZNonHomePageBannerUi(nvHomePageCombinedData.data.homeBanner)
                        }
                    }

                    item {
                        Text(text = "Categories", style = zustTypographySecondary.h1, modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp))
                    }
                    zNonVegHomeCategoryUi(nvHomePageCombinedData.data?.categories)

                    nvHomePageCombinedData.data?.homeBanner?.let {
                        item {
                            Spacer(modifier = Modifier.height(dp_12))
                        }
                        item {
                            Text(text = "Offers for you", modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = dp_16, vertical = dp_8), style = ZustTypography.h1)
                        }
                        item(NV_HOME_OFFER) {
                            ZNonVegHomeOfferUi(nvHomePageCombinedData.data.homeBanner)
                        }
                    }
                    item {
                        ZNonVegHomeTagUi()
                    }

                }

            }
        }

        is NvHomePageCombinedUiModel.Error -> {
            AppUtility.showToast(context = LocalContext.current, nvHomePageCombinedData.errorMessage)
            NonVegHomePageErrorUi {
                viewModel.getUserSavedAddress()
            }
        }

        is NvHomePageCombinedUiModel.Initial -> {

        }
    }

}

@Composable
private fun ShowNonVegProgressBar() {
    Column(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(modifier = Modifier.size(dp_32))
        Spacer(modifier = Modifier.height(dp_12))
        Text(text = "Loading...", style = ZustTypography.body2)
    }
}

@Composable
private fun NonVegHomePageErrorUi(retryCallback: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(onClick = {
            retryCallback.invoke()
        }) {
            Text(text = "Try Again", style = ZustTypography.body1)
        }
    }
}
