package `in`.opening.area.zustapp.address.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.NEW_ADDRESS_ADD
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.NEW_ADDRESS_ADD_ERROR
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.locationManager.models.CustomLocationModel
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.CurrentLocationUi
import `in`.opening.area.zustapp.uiModels.SaveUserAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.address.model.SearchAddressModel

@Composable
fun AddNewAddressUi(
    modifier: Modifier, viewModel: AddressViewModel,
    callback: (Any) -> Unit,
) {
    val saveAddressUiData by viewModel.saveUserAddressUiState.collectAsState(initial = SaveUserAddressUi.InitialUi(false))
    val currentLocationUi by viewModel.currentLocationUiState.collectAsState(initial = CurrentLocationUi.InitialUi(false))
    val context = LocalContext.current
    var inputPinCode by rememberSaveable {
        mutableStateOf("")
    }
    var houseAndFloor by rememberSaveable {
        mutableStateOf("")
    }

    val landmarkAndArea by rememberSaveable {
        mutableStateOf("")
    }

    var alternateMobileNumber by rememberSaveable {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var customLocationModel by remember {
        mutableStateOf(CustomLocationModel())
    }

    isLoading = saveAddressUiData.isLoading || currentLocationUi.isLoading

    when (val data = saveAddressUiData) {
        is SaveUserAddressUi.SaveAddressUi -> {
            if (data.data?.id != null) {
                callback.invoke(data.data)
                AppUtility.showToast(context, "Address saved successfully")
            } else {
                FirebaseAnalytics.logEvents(NEW_ADDRESS_ADD_ERROR)
                AppUtility.showToast(context, "Something went wrong")
            }
        }

        is SaveUserAddressUi.ErrorUi -> {
            AppUtility.showToast(context, data.errors.getTextMsg())
        }

        is SaveUserAddressUi.InitialUi -> {
        }
    }

    when (val currentLocation = currentLocationUi) {
        is CurrentLocationUi.ReceivedCurrentLocation -> {
            if (currentLocation.data != null) {
                customLocationModel = currentLocation.data
                if (!currentLocation.data.pinCode.isNullOrEmpty()) {
                    //  inputPinCode = currentLocation.data.pinCode!!
                }
            }
        }

        is CurrentLocationUi.ErrorState -> {
            customLocationModel = CustomLocationModel()
        }

        else -> {}
    }
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (progressBar, progressBar1) = createRefs()
        val (newAddressContainer) = createRefs()
        val (titleText) = createRefs()
        val (subTitleText) = createRefs()
        val (navIcon) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = "back",
            tint = colorResource(id = R.color.black_2), modifier = Modifier
                .constrainAs(navIcon) {
                    end.linkTo(parent.end, dp_16)
                    bottom.linkTo(titleText.bottom)
                    top.linkTo(titleText.top)
                }
                .size(24.dp)
                .clickable {
                    callback.invoke(FINISH_PAGE)
                }
        )
        Text(text = "Please enter your address",
            style = ZustTypography.bodyLarge,
            color = colorResource(id = R.color.app_black),
            modifier = modifier.constrainAs(titleText) {
                top.linkTo(parent.top, dp_20)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            })

        Text(text = "Required fields are marked with an asterisk *",
            modifier = modifier.constrainAs(subTitleText) {
                top.linkTo(titleText.bottom, dp_4)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodySmall,
            fontWeight = FontWeight.W400,
            color = colorResource(id = R.color.app_black))

        Column(modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .constrainAs(newAddressContainer) {
                top.linkTo(subTitleText.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }) {
            Spacer(modifier = modifier.height(8.dp))
            Button(onClick = {
                callback.invoke(CURRENT_LOCATION)
            }, modifier = modifier
                .clip(shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_black))) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_my_location_24),
                    contentDescription = "",
                    modifier = modifier.size(20.dp),
                    tint = colorResource(id = R.color.white))
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Use current location",
                    modifier = modifier.padding(vertical = 6.dp),
                    color = colorResource(id = R.color.white),
                    style = ZustTypography.bodyMedium)
            }
            if (!customLocationModel.addressLine.isNullOrEmpty()) {
                Spacer(modifier = modifier.height(8.dp))
                Text(text = customLocationModel.addressLine!!,
                    style = ZustTypography.bodyMedium,
                    color = colorResource(id = R.color.app_black))
            }
            Spacer(modifier = modifier.height(16.dp))

            Text(text = "Enter PinCode*",
                style = ZustTypography.bodySmall,
                color = colorResource(id = R.color.black_3))
            Spacer(modifier = modifier.height(6.dp))
            TextField(value = inputPinCode,
                textStyle = ZustTypography.bodyMedium,
                onValueChange = {
                    inputPinCode = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 1)

            Spacer(modifier = modifier.height(16.dp))

            Text(text = "Enter complete address*",
                style = ZustTypography.bodySmall,
                color = colorResource(id = R.color.black_3))

            Spacer(modifier = modifier.height(6.dp))

            TextField(value = houseAndFloor,
                textStyle = ZustTypography.bodyMedium,
                onValueChange = {
                    houseAndFloor = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 3)

            Spacer(modifier = modifier.height(16.dp))

            Text(text = "Alternate Mobile number",
                style = ZustTypography.bodySmall,
                color = colorResource(id = R.color.black_3))
            Spacer(modifier = modifier.height(6.dp))

            TextField(value = alternateMobileNumber,
                textStyle = ZustTypography.bodyMedium,
                onValueChange = {
                    alternateMobileNumber = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 1)

            Spacer(modifier = modifier.height(24.dp))

            OutlinedButton(onClick = {
                if (validateAddressLocally(inputPinCode, houseAndFloor, landmarkAndArea, context)) {
                    if (!viewModel.checkIsSaveAddressAlreadyGoing()) {
                        val bundle = Bundle()
                        bundle.putString("pincode", inputPinCode)
                        bundle.putString("houseAndFloor", houseAndFloor)
                        bundle.putString("landmark", landmarkAndArea)
                        bundle.putDouble("latitude", customLocationModel.lat ?: 0.0)
                        bundle.putDouble("longitude", customLocationModel.lng ?: 0.0)
                        FirebaseAnalytics.logEvents(NEW_ADDRESS_ADD, bundle)
                        viewModel.userAddressInputCache.apply {
                            pinCode = inputPinCode
                            houseNumberAndFloor = houseAndFloor
                            landmark = ""
                            if (customLocationModel.addressLine != null) {
                                description = customLocationModel.addressLine
                            }
                            if (customLocationModel.lat != null) {
                                latitude = customLocationModel.lat
                            }
                            if (customLocationModel.lng != null) {
                                longitude = customLocationModel.lng
                            }
                            optionalMobileNumber = alternateMobileNumber
                        }
                        viewModel.checkAndSaveAddressWithServer()
                    } else {
                        AppUtility.showToast(context, "Please wait")
                    }
                }
            }, modifier = modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
                Text(text = "Save Address",
                    modifier = modifier.padding(vertical = 6.dp),
                    color = colorResource(id = R.color.white),
                    style = ZustTypography.bodyLarge)
            }
        }
        if (isLoading) {
            LinearProgressIndicator(modifier =
            modifier.constrainAs(progressBar) {
                top.linkTo(subTitleText.bottom, dp_8)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            }, color = colorResource(id = R.color.light_blue))

            CustomAnimatedProgressBar(modifier = modifier
                .size(100.dp)
                .constrainAs(progressBar1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        }
    }
}

private fun validateAddressLocally(
    enteredPinCode: String?,
    houseNumberAndFloor: String?,
    landmark: String?, context: Context,
): Boolean {
    if (enteredPinCode.isNullOrEmpty()) {
        AppUtility.showToast(context, "Please enter Pincode")
        return false
    }
    if (enteredPinCode.length != 6) {
        AppUtility.showToast(context, "Please enter correct PinCode")
        return false
    }
    if (houseNumberAndFloor.isNullOrEmpty()) {
        AppUtility.showToast(context, "Please enter full Address")
        return false
    }
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun getTextFieldColors(): TextFieldColors {
    val containerColor = Color(0xffEFEFEF)
    return TextFieldDefaults.colors(
        focusedTextColor = colorResource(id = R.color.black_3),
        focusedContainerColor = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor = containerColor,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )
}


@Composable
fun AddNewAddressUiV2(
    modifier: Modifier, viewModel: AddressViewModel,
    addressModel: SearchAddressModel?,
    searchAddressModel: CustomLocationModel,
    callback: (Any) -> Unit,
) {
    val saveAddressUiData by viewModel.saveUserAddressUiState.collectAsState(initial = SaveUserAddressUi.InitialUi(false))
    val context = LocalContext.current

    var inputPinCode by rememberSaveable {
        mutableStateOf(searchAddressModel.pinCode ?: "")
    }

    var houseAndFloor by rememberSaveable {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val customLocationModel by remember {
        mutableStateOf(searchAddressModel)
    }

    var alternateMobileNumber by rememberSaveable {
        mutableStateOf("")
    }

    isLoading = saveAddressUiData.isLoading

    when (val data = saveAddressUiData) {
        is SaveUserAddressUi.SaveAddressUi -> {
            if (data.data?.id != null) {
                callback.invoke(data.data)
                AppUtility.showToast(context, "Address saved successfully")
            } else {
                FirebaseAnalytics.logEvents(NEW_ADDRESS_ADD_ERROR)
                AppUtility.showToast(context, "Something went wrong")
            }
        }

        is SaveUserAddressUi.ErrorUi -> {
            AppUtility.showToast(context, data.errors.getTextMsg())
        }

        is SaveUserAddressUi.InitialUi -> {
        }
    }

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (progressBar, progressBar1) = createRefs()
        val (newAddressContainer) = createRefs()
        val (titleText) = createRefs()
        val (subTitleText) = createRefs()
        val (navIcon) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = "back",
            tint = colorResource(id = R.color.black_2), modifier = Modifier
                .constrainAs(navIcon) {
                    end.linkTo(parent.end, dp_16)
                    bottom.linkTo(titleText.bottom)
                    top.linkTo(titleText.top)
                }
                .size(24.dp)
                .clickable {
                    callback.invoke(FINISH_PAGE)
                }
        )

        Text(text = "Please enter your address",
            style = ZustTypography.bodyLarge,
            color = colorResource(id = R.color.app_black),
            modifier = modifier.constrainAs(titleText) {
                top.linkTo(parent.top, dp_20)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            })


        Text(text = "Required fields are marked with an asterisk *",
            modifier = modifier.constrainAs(subTitleText) {
                top.linkTo(titleText.bottom, dp_4)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodySmall,
            fontWeight = FontWeight.W400,
            color = colorResource(id = R.color.app_black))

        Column(modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .constrainAs(newAddressContainer) {
                top.linkTo(subTitleText.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }) {

            Spacer(modifier = modifier.height(16.dp))

            if (!searchAddressModel.addressLine.isNullOrEmpty()) {
                Box(modifier = Modifier.background(color = colorResource(id = R.color.app_black),
                    shape = RoundedCornerShape(8.dp))) {
                    Text(text = searchAddressModel.addressLine!!,
                        style = ZustTypography.bodyLarge,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp))
                }
                Spacer(modifier = modifier.height(16.dp))
            }

            Text(text = "Enter PinCode*",
                style = ZustTypography.bodySmall,
                color = colorResource(id = R.color.black_3))

            Spacer(modifier = modifier.height(6.dp))

            TextField(value = inputPinCode,
                textStyle = ZustTypography.bodyMedium,
                onValueChange = {
                    inputPinCode = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 1)

            Spacer(modifier = modifier.height(16.dp))

            Text(text = if (addressModel?.apartmentData != null) {
                "Enter Floor Number*"
            } else {
                "Enter Complete Address*"
            },
                style = ZustTypography.bodySmall,
                color = colorResource(id = R.color.black_3))
            Spacer(modifier = modifier.height(6.dp))
            TextField(value = houseAndFloor,
                textStyle = ZustTypography.bodyMedium,
                onValueChange = {
                    houseAndFloor = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 3)

            Spacer(modifier = modifier.height(16.dp))

            Text(text = "Alternate Mobile number",
                style = ZustTypography.bodySmall,
                color = colorResource(id = R.color.black_3))
            Spacer(modifier = modifier.height(6.dp))

            TextField(value = alternateMobileNumber,
                textStyle = ZustTypography.bodyMedium,
                onValueChange = {
                    alternateMobileNumber = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 1)

            Spacer(modifier = modifier.height(24.dp))

            OutlinedButton(onClick = {
                if (validateAddressLocally(inputPinCode, houseAndFloor, null, context)) {
                    if (!viewModel.checkIsSaveAddressAlreadyGoing()) {
                        val bundle = Bundle()
                        bundle.putString("pincode", inputPinCode)
                        bundle.putString("houseAndFloor", houseAndFloor)
                        bundle.putDouble("latitude", customLocationModel.lat ?: 0.0)
                        bundle.putDouble("longitude", customLocationModel.lng ?: 0.0)
                        FirebaseAnalytics.logEvents(NEW_ADDRESS_ADD, bundle)
                        viewModel.userAddressInputCache.apply {
                            pinCode = inputPinCode
                            houseNumberAndFloor = houseAndFloor
                            landmark = ""
                            if (customLocationModel.addressLine != null) {
                                description = customLocationModel.addressLine
                            }
                            if (customLocationModel.lat != null) {
                                latitude = customLocationModel.lat
                            }
                            if (customLocationModel.lng != null) {
                                longitude = customLocationModel.lng
                            }
                            optionalMobileNumber = alternateMobileNumber
                            is_high_priority = addressModel?.apartmentData != null
                        }
                        viewModel.checkAndSaveAddressWithServer()
                    } else {
                        AppUtility.showToast(context, "Please wait")
                    }
                }
            }, modifier = modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
                Text(text = "Save Address",
                    modifier = modifier.padding(vertical = 6.dp),
                    color = colorResource(id = R.color.white),
                    style = ZustTypography.bodyLarge)
            }
        }
        if (isLoading) {
            LinearProgressIndicator(modifier =
            modifier.constrainAs(progressBar) {
                top.linkTo(subTitleText.bottom, dp_8)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
                width = Dimension.fillToConstraints
            }, color = colorResource(id = R.color.light_blue))

            CustomAnimatedProgressBar(modifier = modifier
                .size(100.dp)
                .constrainAs(progressBar1) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })
        }
    }
}
