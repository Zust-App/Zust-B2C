package `in`.opening.area.zustapp.locationV2

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.home.components.FullScreenErrorUi
import `in`.opening.area.zustapp.locationV2.models.ApartmentData
import `in`.opening.area.zustapp.locationV2.uistate.ApartmentListingUiState
import `in`.opening.area.zustapp.locationV2.viewModel.ApartmentListingViewModel
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import non_veg.payment.ui.ViewSpacer8
import wallet.modifier

@Composable
fun ApartmentListingContainerUi(apartmentListingViewModel: ApartmentListingViewModel, callback: (ApartmentData) -> Unit) {
    val apartmentListingData by apartmentListingViewModel.apartmentListingUiState.collectAsState()
    when (val data = apartmentListingData) {
        is ApartmentListingUiState.Success -> {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()) {
                item {
                    ViewSpacer8()
                }
                item {
                    Text(text = "Registered Apartment", style = ZustTypography.bodyLarge,
                        color = colorResource(id = R.color.new_material_primary),
                        modifier = Modifier.padding(horizontal = dp_12, vertical = dp_12))
                }
                item {
                    Divider(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp))
                }
                data.data?.forEach { apartmentData ->
                    item {
                        ApartmentSingleItemUi(apartmentData) {
                            callback.invoke(it)
                        }
                    }
                    item {
                        ViewSpacer8()
                    }
                }
            }
        }

        is ApartmentListingUiState.Error -> {
            FullScreenErrorUi(errorCode = -1, retryCallback = {
                apartmentListingViewModel.getApartmentListing()
            }) {

            }
        }

        is ApartmentListingUiState.Initial -> {
            ConstraintLayout(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()) {
                val (progressBar, text) = createRefs()
                CustomAnimatedProgressBar(modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                })

                Text(text = "Loading Apartment List...", modifier = Modifier.constrainAs(text) {
                    top.linkTo(progressBar.bottom, dp_8)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                }, style = ZustTypography.bodySmall, color = colorResource(id = R.color.new_hint_color))
            }
        }

    }
}

@Composable
private fun ApartmentSingleItemUi(apartmentData: ApartmentData, callback: (ApartmentData) -> Unit) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = dp_12, vertical = dp_8)
        .clickable {
            callback.invoke(apartmentData)
        }) {
        val (apartmentName, apartmentAddress, icon) = createRefs()
        Text(text = apartmentData.apartmentName ?: "", modifier = Modifier.constrainAs(apartmentName) {
            top.linkTo(parent.top)
            start.linkTo(icon.end, dp_12)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        }, style = ZustTypography.bodyMedium, color = colorResource(id = R.color.app_black))
        Text(
            text = apartmentData.address ?: "",
            modifier = Modifier.constrainAs(apartmentAddress) {
                top.linkTo(apartmentName.bottom, dp_6)
                start.linkTo(icon.end, dp_12)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            },
            style = ZustTypography.bodySmall, color = colorResource(id = R.color.new_hint_color),
        )
        Icon(painter = painterResource(id = R.drawable.outline_apartment_24),
            contentDescription = "",
            modifier = Modifier
                .size(dp_24)
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            tint = colorResource(id = R.color.new_hint_color))
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {

    }
}