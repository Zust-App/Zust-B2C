package `in`.opening.area.zustapp.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.flowlayout.FlowRow
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.payment.models.CreatePaymentDataModel
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.payment.models.convertToCartSummaryData
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.uiModels.CreatePaymentUi
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.PaymentActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import non_veg.cart.ui.NonVegBillingContainerDataHolder
import non_veg.payment.ui.DeliveryTimingOfferInfoUi
import non_veg.payment.ui.NonVegPaymentAddressUi
import non_veg.payment.ui.CommonPaymentMethodItemUi
import non_veg.payment.ui.ShowPaymentPageUiShimmer
import non_veg.payment.ui.ViewSpacer20

private val paymentMethodItemModifier = Modifier
    .wrapContentHeight()
    .wrapContentWidth()
    .padding(horizontal = dp_12)
    .background(color = Color.White)

@Composable
fun GroceryPaymentPageMainUi(paddingValues: PaddingValues, paymentViewModel: PaymentActivityViewModel, paymentMethodCallback: (PaymentMethod) -> Unit, firstCallback: (CreatePaymentDataModel) -> Unit) {
    val paymentDataUi by paymentViewModel.paymentMethodUiState.collectAsState()

    val createPaymentUiState by paymentViewModel.createPaymentUiState.collectAsState()
    val context = LocalContext.current
    when (val response = createPaymentUiState) {
        is CreatePaymentUi.CreateSuccess -> {
            firstCallback.invoke(response.data)
        }

        is CreatePaymentUi.ErrorUi -> {
            if (!response.errorMsg.isNullOrEmpty()) {
                AppUtility.showToast(context, response.errorMsg)
            } else {
                AppUtility.showToast(context, response.errors.getTextMsg())
            }
            paymentViewModel.createPaymentUiState.update {
                CreatePaymentUi.InitialUi(false)
            }
        }

        else -> {

        }
    }
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {
        val (data, loader) = createRefs()
        when (val paymentResponse = paymentDataUi) {
            is PaymentMethodUi.MethodSuccess -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .constrainAs(data) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
                    .background(color = colorResource(id = R.color.screen_surface_color))
                    .padding(paddingValues)) {
                    if (paymentResponse.data.isNotEmpty()) {
                        item {
                            DeliveryTimingOfferInfoUi(paymentViewModel.paymentActivityReqData?.expectedDelivery)
                        }
                        item {
                            ViewSpacer20()
                        }

                        paymentResponse.data.forEach { data ->
                            item {
                                Text(
                                    text = data.paymentCategory,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = dp_16, vertical = dp_8),
                                    style = ZustTypography.titleMedium, color = colorResource(id = R.color.app_black))
                            }
                            if (data.alignment == "horizontal") {
                                item {
                                    FlowRow(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .background(color = colorResource(id = R.color.white))
                                        .padding(horizontal = dp_16, vertical = dp_16)) {
                                        data.paymentMethods.forEach {
                                            CommonPaymentMethodItemUi(paymentMethodItemModifier, it.key, it.name, it.thumbnail ?: "", it.isSelected ?: false) {
                                                paymentMethodCallback.invoke(it)
                                            }
                                        }
                                    }
                                }
                            } else {
                                item {
                                    Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .background(color = colorResource(id = R.color.white))
                                        .padding(horizontal = dp_16, vertical = dp_16)) {
                                        data.paymentMethods.forEach {
                                            CommonPaymentMethodItemUi(paymentMethodItemModifier, it.key, it.name, it.thumbnail ?: "", it.isSelected ?: false) {
                                                paymentMethodCallback.invoke(it)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            ViewSpacer20()
                        }
                        item {
                            paymentViewModel.paymentActivityReqData?.let {
                                NonVegBillingContainerDataHolder(it.convertToCartSummaryData(), Modifier
                                    .padding(horizontal = 16.dp)
                                    .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(dp_8))
                                    .padding(horizontal = dp_16, vertical = dp_16))
                            }
                        }
                        item {
                            ViewSpacer20()
                        }
                        item {
                            NonVegPaymentAddressUi()
                        }
                    }
                }
            }

            is PaymentMethodUi.ErrorUi -> {
                AppUtility.showToast(LocalContext.current, "Something went wrong")
            }

            is PaymentMethodUi.InitialUi -> {

            }

        }

        if (createPaymentUiState.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .constrainAs(loader) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
    if (paymentDataUi.isLoading) {
        ShowPaymentPageUiShimmer()
    }
}


suspend fun fetchIdsFromApi(): List<Int> {
    return arrayListOf()
}

var x = arrayListOf<Any>()

// Function to fetch details for a single ID from the API
suspend fun fetchDetailsByIdFromApi(id: Int): String {
    val y = x.remove {

    }
    fetchDetailsForIds().collectLatest {

    }
    return ""
}


fun fetchDetailsForIds(): Flow<List<String>> = flow {
    val ids = fetchIdsFromApi()
    val x = ids
        .map { id ->
            val details = fetchDetailsByIdFromApi(id)
            details
        }
    emit(x)
}


fun <T, R> List<T>.remove(filter: T.() -> R): List<T> {
    return arrayListOf<T>()
}