package zustbase.basepage.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.components.homePageBrandPromiseUi
import `in`.opening.area.zustapp.home.components.homeSuggestProductUi
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.home.ui.homeGenericPageSearchDefaultUi
import non_veg.payment.ui.ViewSpacer8
import zustbase.ZustLandingViewModel
import zustbase.services.models.ZustService
import zustbase.services.uiModel.ZustAvailServicesUiModel

@Composable
fun ZustBasePageMainUi(zustLandingViewModel: ZustLandingViewModel, genericCallback: (ACTION) -> Unit, basicCallback: (ZustService) -> Unit) {
    val zustAvailServiceUiModel by zustLandingViewModel.zustServicesUiModel.collectAsState()
    if (zustAvailServiceUiModel.isLoading) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CustomAnimatedProgressBar(modifier = Modifier
                .size(100.dp))
        }
    }
    when (val response = zustAvailServiceUiModel) {
        is ZustAvailServicesUiModel.Success -> {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = colorResource(id = R.color.white))) {
                homeGenericPageSearchDefaultUi(arrayListOf("Search `Grocery`", "Search `Meat`", "Search `Restaurant`", "Search `Milk`")) {

                }
                item {
                    Spacer(modifier = Modifier.height(dp_12))
                }
                if (response.zustServicePageResponse?.data != null && response.zustServicePageResponse.data.isNotEmpty()) {
                    response.zustServicePageResponse.data[0].let {
                        if (!it.list.isNullOrEmpty()) {
                            item {
                                NvHomeTitle(it.title)
                                ViewSpacer8()
                            }
                            if (it.key == "promotion") {
                                zustBaseRow2ItemsUi(it.list)
                            } else {
                                item {
                                    ZustBaseAutoScrollUi(it.list)
                                }
                            }
                        }
                    }
                }

                if (!response.data?.serviceList.isNullOrEmpty()) {
                    item {
                        NvHomeTitle("We deliver essential to your doorstep")
                        ViewSpacer8()
                    }
                    zustAvailServicesUi(response.data, basicCallback)
                }

                if ((response.zustServicePageResponse?.data?.size ?: 0) > 1) {
                    response.zustServicePageResponse?.data?.get(1)?.let {
                        if (!it.list.isNullOrEmpty()) {
                            item {
                                NvHomeTitle(it.title)
                            }
                            if (it.key == "promotion") {
                                zustBaseRow2ItemsUi(it.list)
                            } else {
                                item {
                                    ZustBaseAutoScrollUi(it.list)
                                }
                            }
                        }
                    }
                }
                if ((response.zustServicePageResponse?.data?.size ?: 0) > 2) {
                    response.zustServicePageResponse?.data?.get(2)?.let {
                        if (!it.list.isNullOrEmpty()) {
                            item {
                                NvHomeTitle(it.title)
                            }
                            if (it.key == "promotion") {
                                zustBaseRow2ItemsUi(it.list)
                            } else {
                                item {
                                    ZustBaseAutoScrollUi(it.list)
                                }
                            }
                        }
                    }
                }

                homeSuggestProductUi(genericCallback)

                homePageBrandPromiseUi(true)
            }
        }

        is ZustAvailServicesUiModel.Empty -> {

        }

        is ZustAvailServicesUiModel.ErrorUi -> {
            AppUtility.showToast(LocalContext.current, response.message)
        }
    }
}

@Composable
private fun NvHomeTitle(title: String?) {
    if (title == null) {
        return
    }
    Text(text = title,
        style = ZustTypography.bodyLarge, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = dp_16, top = dp_16, end = dp_16, bottom = dp_8))
}
