package `in`.opening.area.zustapp.address.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.uiModels.UserSavedAddressUi
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.locationV2.viewModel.LocationPermissionViewModel
import `in`.opening.area.zustapp.product.model.Address

const val KEY_NEW_ADDRESS = "new"
const val KEY_SAVED_ADDRESS = "saved"
const val CURRENT_LOCATION = "current_location"
const val FINISH_PAGE = "finish"

@Composable
fun AlreadyAddedAddressListUi(
    modifier: Modifier, viewModel: LocationPermissionViewModel,
    paddingValues: PaddingValues, callback: (AddressItem) -> Unit,
) {
    val savedAddressData by viewModel.userAddressListUiState.collectAsState(initial = UserSavedAddressUi.InitialUi(isLoading = false))
    var isLoading by remember {
        mutableStateOf(false)
    }
    isLoading = savedAddressData.isLoading
    ConstraintLayout(modifier = modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .padding(paddingValues)) {
        val (progressBar) = createRefs()
        when (val data = savedAddressData) {
            is UserSavedAddressUi.UserAddressResponse -> {
                if (!data.data?.addresses.isNullOrEmpty()) {
                    val (title, addNewAddressTag, list) = createRefs()
                    Text(text = "Saved Location", modifier = modifier.constrainAs(title) {
                        start.linkTo(parent.start, dp_16)
                        top.linkTo(parent.top, dp_16)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, style = ZustTypography.body1, color = colorResource(id = R.color.app_black))
                    LazyColumn(modifier = modifier
                        .fillMaxWidth()
                        .constrainAs(list) {
                            top.linkTo(title.bottom, dp_16)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(addNewAddressTag.top, dp_16)
                            height = Dimension.fillToConstraints
                        }) {
                        data.data?.addresses?.forEach {
                            item(key = it.id) {
                                SavedAddressItemUi(modifier = modifier, it) { address ->
                                    callback.invoke(address)
                                }
                                Divider(modifier = modifier
                                    .height(0.5.dp)
                                    .background(color = Color(0xffCDCDCD)))
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }

            is UserSavedAddressUi.ErrorState -> {
                val (retryButton) = createRefs()
                Button(onClick = { viewModel.getAllAddress() },
                    modifier = Modifier.constrainAs(retryButton) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    Text(text = "Retry Again")
                }
            }

            is UserSavedAddressUi.InitialUi -> {

            }
        }
        if (isLoading) {
            CircularProgressIndicator(modifier = modifier
                .size(30.dp)
                .constrainAs(progressBar) {
                    top.linkTo(parent.top, dp_20)
                    end.linkTo(parent.end, dp_20)
                    start.linkTo(parent.start, dp_20)
                    bottom.linkTo(parent.bottom, dp_20)
                })
        }
    }
}


