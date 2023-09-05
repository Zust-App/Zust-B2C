package non_veg.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.composeContainer.HomePageShimmerUi
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_32
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.home.uiModel.NvHomePageCombinedUiModel
import non_veg.home.viewmodel.ZustNvEntryViewModel
import non_veg.payment.ui.ViewSpacer20
import ui.colorBlack

private const val NV_HOME_OFFER = 1
private const val NV_HOME_BANNER = 2

@Composable
fun ZustNvEntryMainUi(
    viewModel: ZustNvEntryViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    paddingValues: PaddingValues,
    changeLocation: () -> Unit,
    searchCallback: () -> Unit,
) {
    val nvHomePageCombinedData = viewModel.nonVegHomePageUiModel.collectAsState().value

    if (nvHomePageCombinedData.isLoading) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(paddingValues)) {
            HomePageShimmerUi()
            ShowNonVegProgressBar()
        }
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
                        Text(text = "Categories", style = ZustTypography.bodyLarge,
                            modifier = Modifier.padding(top = 24.dp,
                                start = 16.dp,
                                end = 16.dp, bottom = dp_12))
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
                                .padding(horizontal = dp_16, vertical = dp_8),
                                style = ZustTypography.bodyMedium)
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
            NonVegHomePageErrorUi(retryCallback = {
                viewModel.getUserSavedAddress()
            }, changeLocation = {
                changeLocation.invoke()
            }, nvHomePageCombinedData.errorCode)
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
        CircularProgressIndicator(modifier = Modifier.size(dp_32),
            color = colorResource(id = R.color.new_material_primary))
        Spacer(modifier = Modifier.height(dp_12))
        Text(text = "Loading...", style = ZustTypography.bodyMedium)
    }
}

@Composable
private fun NonVegHomePageErrorUi(retryCallback: () -> Unit, changeLocation: () -> Unit, errorCode: Int?) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        if (errorCode == ApiRequestManager.NOT_COVERAGE_ERROR_CODE) {
            Text(text = "Service not available in \nyou area currently",
                color = colorResource(id = R.color.black),
                style = ZustTypography.titleLarge,
                modifier = Modifier.padding(horizontal = dp_16, vertical = 8.dp),
                textAlign = TextAlign.Center)

            Text(text = "But we are working on it.\nSo Hold on!", color = colorResource(id = R.color.black_3),
                style = ZustTypography.bodySmall.copy(fontSize = 16.sp),
                modifier = Modifier.padding(horizontal = dp_16),
                textAlign = TextAlign.Center)

            ViewSpacer20()

            Image(painter = painterResource(id = R.drawable.no_service_available), contentDescription = "no internet")

            ViewSpacer20()

            OutlinedButton(onClick = {
                changeLocation.invoke()
            }, modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(), border = BorderStroke(color = colorResource(id = R.color.new_hint_color), width = 1.dp)) {
                Text(text = "Change Location",
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = colorBlack)
            }
        } else {

            Text(text = "Oops, Something Went \n" +
                    "wrong!",
                color = colorResource(id = R.color.black),
                style = ZustTypography.titleLarge,
                modifier = Modifier.padding(horizontal = dp_16, vertical = 8.dp),
                textAlign = TextAlign.Center)

            Text(text = "Please try again in sometime.", color = colorResource(id = R.color.black_3),
                style = ZustTypography.bodySmall.copy(fontSize = 16.sp),
                modifier = Modifier.padding(horizontal = dp_16),
                textAlign = TextAlign.Center)

            ViewSpacer20()

            Image(painter = painterResource(id = R.drawable.error_icon), contentDescription = "error icon")

            ViewSpacer20()

            OutlinedButton(onClick = {
                retryCallback.invoke()
            }, modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(), border = BorderStroke(color = colorResource(id = R.color.new_hint_color), width = 1.dp)) {
                Text(text = "Retry",
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = colorBlack)
            }
        }
    }

}
