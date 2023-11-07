package wallet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.home.components.FullScreenErrorUi
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.openWebActivity
import non_veg.payment.ui.ViewSpacer12
import non_veg.payment.ui.ViewSpacer20
import non_veg.payment.ui.ViewSpacer8
import ui.colorWhite
import wallet.data.ZustPayAmountDetail
import wallet.uistate.ZustPayUiState
import wallet.utils.convertToDisplayDate
import zustbase.ZustLandingViewModel
import zustbase.analysis.ui.rectShape16p


val modifier = Modifier
    .fillMaxWidth()
    .wrapContentHeight()
const val rupeesSymbol = "₹"

@Composable
fun ZustPayMainUi(zustLandingViewModel: ZustLandingViewModel) {
    LaunchedEffect(key1 = Unit, block = {
        zustLandingViewModel.retrieveZustPayWallet()
    })
    val context = LocalContext.current
    val uiData by zustLandingViewModel.zustPayUiModel.collectAsState()
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(horizontal = dp_16),
        horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Text(text = "ZustPay",
                color = colorResource(id = R.color.app_black),
                style = ZustTypography.titleLarge, modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dp_8))
        }

        when (val data = uiData) {
            is ZustPayUiState.Success -> {
                item {
                    Row(modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentHeight()) {
                        ReferralIncome(data.data ?: ZustPayAmountDetail(), data.totalReferralIncome)
                    }
                    Spacer(modifier = Modifier.height(dp_16))
                }

                item {
                    ViewSpacer20()
                }
                item {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Referral Details",
                            style = ZustTypography.bodyLarge, color = colorResource(id = R.color.app_black))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = ("Total Affiliate Income:-" + rupeesSymbol + (data.totalReferralIncome)),
                            style = ZustTypography.bodyLarge,
                            color = colorResource(id = R.color.new_material_primary))
                    }
                }
                if (data.referralDetails.isEmpty()) {
                    item {
                        ViewSpacer12()
                    }
                    item {
                        Text(text = "You have not referred to anyone yet now. Please refer ZustApp and start Earning",
                            style = ZustTypography.bodyMedium,
                            color = colorResource(id = R.color.new_hint_color))
                        ViewSpacer12()
                    }
                }
                for ((level, details) in data.referralDetails) {
                    for (detail in details) {
                        item {
                            OutlinedCard(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(vertical = dp_8),
                                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white)),
                                elevation = CardDefaults.elevatedCardElevation(2.dp)) {
                                ConstraintLayout(modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(dp_12)) {
                                    val (
                                        joinLevel,
                                        phoneNum,
                                        name,
                                        joiningDate, orderAmount,
                                    ) = createRefs()


                                    Text(text = "L" + detail.level, style = ZustTypography.bodyLarge,
                                        modifier = Modifier
                                            .constrainAs(joinLevel) {
                                                start.linkTo(parent.start)
                                                top.linkTo(parent.top)
                                                bottom.linkTo(parent.bottom)
                                            }
                                            .background(shape = RoundedCornerShape(dp_8),
                                                color = colorResource(id = R.color.new_material_primary))
                                            .padding(dp_8),
                                        color = colorResource(id = R.color.white))

                                    Text(text = detail.refereeName ?: "",
                                        style = ZustTypography.bodyMedium,
                                        color = colorResource(id = R.color.app_black),
                                        modifier = Modifier.constrainAs(name) {
                                            top.linkTo(parent.top)
                                            start.linkTo(joinLevel.end, dp_12)
                                            end.linkTo(joiningDate.start, dp_8)
                                            width = Dimension.fillToConstraints
                                        }, maxLines = 1)

                                    Text(text = convertToDisplayDate(detail.joiningDate),
                                        style = ZustTypography.bodySmall.copy(fontStyle = FontStyle.Italic),
                                        color = colorResource(id = R.color.new_hint_color),
                                        modifier = Modifier.constrainAs(joiningDate) {
                                            top.linkTo(parent.top)
                                            end.linkTo(parent.end)
                                            start.linkTo(name.end, dp_8)
                                        }, textAlign = TextAlign.End)

                                    Text(text = detail.phoneNum ?: "+91-XXXXXXXXX",
                                        style = ZustTypography.bodyMedium,
                                        color = colorResource(id = R.color.new_hint_color),
                                        modifier = Modifier.constrainAs(phoneNum) {
                                            top.linkTo(name.bottom, dp_6)
                                            start.linkTo(joinLevel.end, dp_12)
                                            end.linkTo(orderAmount.start, dp_8)
                                            width = Dimension.fillToConstraints
                                        })

                                    Text(text = "Amount:- " + rupeesSymbol + (detail.userOrderDetail?.totalOrderSum ?: 0),
                                        style = ZustTypography.bodyMedium,
                                        color = colorResource(id = R.color.new_hint_color),
                                        modifier = Modifier.constrainAs(orderAmount) {
                                            top.linkTo(name.bottom, dp_6)
                                            start.linkTo(phoneNum.end, dp_12)
                                            end.linkTo(parent.end)
                                            width = Dimension.fillToConstraints
                                        }, textAlign = TextAlign.End)

                                }
                            }
                        }
                        item {
                            ViewSpacer8()
                        }
                    }
                    item {
                        ViewSpacer8()
                    }
                }


                item {
                    Button(
                        onClick = {
                            val link = zustLandingViewModel.getAffiliatePartnerLink()
                            context.openWebActivity(link)
                        }, modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
                        Text(text = "Become affiliate partner",
                            style = ZustTypography.bodyLarge,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = colorWhite)

                    }
                }

            }

            is ZustPayUiState.Error -> {
                item {
                    FullScreenErrorUi(errorCode = -1, retryCallback = {
                        zustLandingViewModel.retrieveZustPayWallet()
                    }) {

                    }
                }
            }

            is ZustPayUiState.Initial -> {
                item {
                    CustomAnimatedProgressBar(modifier = Modifier)
                }
            }
        }
    }
}


@Composable
private fun ReferralIncome(data: ZustPayAmountDetail, totalReferralIncome: Double) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = dp_16, vertical = dp_8)) {
        Column(modifier = Modifier
            .shadow(elevation = dp_8, clip = false, shape = rectShape16p)
            .weight(1f)
            .border(border = BorderStroke(1.dp, color = colorResource(id = R.color.white)), shape = rectShape16p)
            .background(shape = RoundedCornerShape(dp_16),
                color = colorResource(id = R.color.some_silver))
            .padding(vertical = dp_16), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "Wallet\namount", style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(dp_8))
            Text(text = rupeesSymbol + String.format("%.1f", data.walletAmount ?: 0.0), style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.width(dp_16))
        Column(modifier = Modifier
            .shadow(elevation = dp_8, clip = false, shape = rectShape16p)
            .weight(1f)
            .border(border = BorderStroke(1.dp, color = colorResource(id = R.color.white)), shape = rectShape16p)
            .background(shape = rectShape16p,
                color = colorResource(id = R.color.lighting))
            .padding(vertical = dp_16), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(text = "Affiliate\namount", style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(dp_8))
            Text(text = rupeesSymbol + String.format("%.1f", totalReferralIncome), style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)

        }
        Spacer(modifier = Modifier.width(dp_16))

    }
}

