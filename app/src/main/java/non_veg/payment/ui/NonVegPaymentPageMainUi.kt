package non_veg.payment.ui


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.uiModels.PaymentMethodUi
import non_veg.cart.ui.NonVegBillingContainerDataHolder
import non_veg.payment.viewModels.NonVegPaymentViewModel

@Composable
fun NonVegPaymentPageMainUi(paddingValues: PaddingValues, nonVegPaymentViewModel: NonVegPaymentViewModel = viewModel(), paymentMethodCallback: (PaymentMethod) -> Unit) {
    val paymentUiState = nonVegPaymentViewModel.paymentMethodUiState.collectAsState()
    if (paymentUiState.value.isLoading) {
        ShowPaymentPageUiShimmer()
    }
    when (paymentUiState.value) {
        is PaymentMethodUi.MethodSuccess -> {
            LazyColumn(modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = colorResource(id = R.color.screen_surface_color))) {
                item {
                    DeliveryTimingOfferInfoUi(nonVegPaymentViewModel.nonVegCartDetailsForPayment?.expectedDeliveryTime, nonVegPaymentViewModel.getNonVegMinDeliveryAmount())
                }
                item {
                    ViewSpacer20()
                }
                item {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(dp_8))
                        .padding(horizontal = dp_16, vertical = dp_16)) {
                        Text(
                            text = stringResource(R.string.payment_method),
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = ZustTypography.titleMedium, color = colorResource(id = R.color.app_black))
                        ViewSpacer8()
                        (paymentUiState.value as PaymentMethodUi.MethodSuccess).data.forEach {
                            NonVegPaymentMethodUi(it.key, it.name, it.isSelected ?: false) {
                                paymentMethodCallback.invoke(it)
                            }
                        }
                    }
                }
                item {
                    ViewSpacer20()
                }
                item {
                    nonVegPaymentViewModel.cartSummaryData?.let {
                        NonVegBillingContainerDataHolder(it, Modifier
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

        is PaymentMethodUi.ErrorUi -> {

        }

        is PaymentMethodUi.InitialUi -> {

        }
    }
}

@Composable
fun ShowPaymentPageUiShimmer() {
    val shimmerColors = listOf(
        Color(0xffDBDBDB).copy(alpha = 0.8f),
        Color(0xffDBDBDB).copy(alpha = 0.5f),
        Color(0xffDBDBDB).copy(alpha = 1f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = dp_16, vertical = dp_12)) {
        item {
            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Spacer(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Spacer(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }
    }
}

@Composable
fun ViewSeparator() {
    Spacer(modifier = viewSeparatorModifier.background(colorResource(id = R.color.light)))
}

@Composable
fun ViewSpacer20() {
    Spacer(modifier = spacerHeight20Modifier)
}

@Composable
fun ViewSpacer12() {
    Spacer(modifier = spacerHeight12Modifier)
}

@Composable
fun ViewSpacer8() {
    Spacer(modifier = spacerHeight8Modifier)
}

private val spacerHeight20Modifier = Modifier.height(dp_20)
private val spacerHeight12Modifier = Modifier.height(dp_12)
private val spacerHeight8Modifier = Modifier.height(dp_8)

var viewSeparatorModifier = Modifier
    .height(2.dp)
    .fillMaxWidth()
