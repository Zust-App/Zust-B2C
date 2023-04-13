package `in`.opening.area.zustapp.address.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.locationManager.models.CustomLocationModel
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.uiModels.CurrentLocationUi
import `in`.opening.area.zustapp.uiModels.SaveUserAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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

@Composable
fun AddNewAddressUi(
    modifier: Modifier, viewModel: AddressViewModel,
    callback: (Any) -> Unit,
) {
    val saveAddressUiData by viewModel.saveUserAddressUiState.collectAsState(initial = SaveUserAddressUi.InitialUi(false))
    val currentLocationUi by viewModel.currentLocationUiState.collectAsState(initial = CurrentLocationUi.InitialUi(false))

    val context = LocalContext.current
    var inputPinCode by rememberSaveable() {
        mutableStateOf("")
    }
    var houseAndFloor by rememberSaveable() {
        mutableStateOf("")
    }

    val landmarkAndArea by rememberSaveable() {
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
            style = Typography_Montserrat.body1,
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
            }, style = Typography_Montserrat.subtitle1,
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
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.light_black))) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_my_location_24),
                    contentDescription = "",
                    modifier = modifier.size(20.dp),
                    tint = colorResource(id = R.color.white))
                Spacer(modifier = modifier.width(8.dp))
                Text(text = "Use current location",
                    modifier = modifier.padding(vertical = 6.dp),
                    color = colorResource(id = R.color.white),
                    style = Typography_Montserrat.body2)
            }
            if (!customLocationModel.addressLine.isNullOrEmpty()) {
                Spacer(modifier = modifier.height(8.dp))
                Text(text = customLocationModel.addressLine!!,
                    style = Typography_Montserrat.body2,
                    color = colorResource(id = R.color.app_black))
            }
            Spacer(modifier = modifier.height(16.dp))

            Text(text = "Enter PinCode*",
                style = Typography_Montserrat.subtitle1,
                color = colorResource(id = R.color.black_3))
            Spacer(modifier = modifier.height(6.dp))
            TextField(value = inputPinCode,
                textStyle = Typography_Montserrat.body2,
                onValueChange = {
                    inputPinCode = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 1)

            Spacer(modifier = modifier.height(16.dp))

            Text(text = "Enter Complete Address*",
                style = Typography_Montserrat.subtitle1,
                color = colorResource(id = R.color.black_3))
            Spacer(modifier = modifier.height(6.dp))
            TextField(value = houseAndFloor,
                textStyle = Typography_Montserrat.body2,
                onValueChange = {
                    houseAndFloor = it
                }, modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp)),
                colors = getTextFieldColors(), maxLines = 3)

            Spacer(modifier = modifier.height(24.dp))

            OutlinedButton(onClick = {
                if (validateAddressLocally(inputPinCode, houseAndFloor, landmarkAndArea, context)) {
                    if (!viewModel.checkIsSaveAddressAlreadyGoing()) {
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
                        }
                        viewModel.checkAndSaveAddressWithServer()
                    } else {
                        AppUtility.showToast(context, "Please wait")
                    }
                }
            }, modifier = modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.new_material_primary))) {
                Text(text = "Save Address",
                    modifier = modifier.padding(vertical = 6.dp),
                    color = colorResource(id = R.color.white),
                    style = Typography_Montserrat.body1)
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
//    if (landmark.isNullOrEmpty()) {
//        AppUtility.showToast(context, "Please enter Area name")
//        return false
//    }
    return true
}

@Composable
private fun getTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.textFieldColors(
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        backgroundColor = Color(0xffEFEFEF),
        textColor = colorResource(id = R.color.black_3))
}