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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.openWebActivity
import `in`.opening.area.zustapp.webpage.InAppWebActivity
import non_veg.home.ui.text
import ui.colorBlack
import wallet.data.ZustPayAmountDetail
import wallet.data.ZustPayAmountDetailModel
import wallet.uistate.ZustPayUiState
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
    when (uiData) {
        is ZustPayUiState.Success -> {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "ZustPay",
                    color = colorResource(id = R.color.app_black),
                    style = ZustTypography.titleLarge, modifier = Modifier
                        .padding(horizontal = dp_16, vertical = dp_8))

                Row(modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentHeight()) {
                    ReferralIncome(ZustPayAmountDetail())
                }

                Spacer(modifier = Modifier.height(dp_16))

                OutlinedButton(
                    onClick = {
                        val link = zustLandingViewModel.getAffiliatePartnerLink()
                        context.openWebActivity(link)
                    }, modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth(), border = BorderStroke(color = colorResource(id = R.color.new_hint_color), width = 1.dp)) {
                    Text(text = "Become affiliate partner",
                        style = ZustTypography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = colorBlack)

                }
            }
        }

        is ZustPayUiState.Error -> {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "ZustPay",
                    color = colorResource(id = R.color.app_black),
                    style = ZustTypography.titleLarge, modifier = Modifier
                        .padding(horizontal = dp_16, vertical = dp_8))

                Row(modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentHeight()) {
                    ReferralIncome(ZustPayAmountDetail())
                }

                Spacer(modifier = Modifier.height(dp_16))
                OutlinedButton(onClick = {
                    val link = zustLandingViewModel.getAffiliatePartnerLink()
                    context.openWebActivity(link)
                }, modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(), border = BorderStroke(color = colorResource(id = R.color.new_hint_color), width = 1.dp)) {
                    Text(text = "Become affiliate partner",
                        style = ZustTypography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = colorBlack)
                }
            }

        }

        is ZustPayUiState.Initial -> {

        }
    }
}


@Composable
private fun ReferralIncome(data: ZustPayAmountDetail) {
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
            Text(text = "Wallet\nAmount", style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)
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
            Text(text = "Affiliate\nAmount", style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(dp_8))
            Text(text = rupeesSymbol + String.format("%.1f", data.referralAmount ?: 0.0), style = ZustTypography.bodyMedium, textAlign = TextAlign.Center)

        }
        Spacer(modifier = Modifier.width(dp_16))

    }
}

