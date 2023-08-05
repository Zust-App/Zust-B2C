package non_veg.payment.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import non_veg.cart.ui.NonVegBillingContainerDataHolder
import non_veg.payment.viewModels.NonVegPaymentViewModel

@Composable
fun NonVegPaymentPageMainUi(paddingValues: PaddingValues, nonVegPaymentViewModel: NonVegPaymentViewModel = viewModel()) {
    val paymentUiState = nonVegPaymentViewModel.paymentMethodUiState.collectAsState()
    when (paymentUiState.value) {
        is PaymentMethodUi.MethodSuccess -> {
            LazyColumn(modifier = Modifier
                .padding(paddingValues)
                .background(color = colorResource(id = R.color.white))) {
                item {
                    DeliveryTimingOfferInfoUi()
                }
                item {
                    Text(
                        text = stringResource(R.string.payment_method),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        style = ZustTypography.h1)
                }
                item {
                    (paymentUiState.value as PaymentMethodUi.MethodSuccess).data.forEach {
                        NonVegPaymentMethodUi(it.key, it.name, it.isSelected ?: false) {
                            nonVegPaymentViewModel.updatePaymentOptions(it)
                        }
                    }
                }
                item {
                    nonVegPaymentViewModel.cartSummaryData?.let {
                        NonVegBillingContainerDataHolder(it)
                    }
                }
            }
        }

        is PaymentMethodUi.ErrorUi -> {

        }

        is PaymentMethodUi.InitialUi -> {

        }
    }
}