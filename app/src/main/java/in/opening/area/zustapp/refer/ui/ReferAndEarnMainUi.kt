package `in`.opening.area.zustapp.refer.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.refer.ReferAndEarnViewModel
import `in`.opening.area.zustapp.ui.theme.dp_16
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.compose.CustomAnimatedProgressBar
import `in`.opening.area.zustapp.home.components.FullScreenErrorUi
import `in`.opening.area.zustapp.refer.uistate.UserReferralDetailUiState
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.payment.ui.ViewSpacer12
import non_veg.payment.ui.ViewSpacer20
import non_veg.payment.ui.ViewSpacer8
import wallet.rupeesSymbol
import wallet.utils.convertToDisplayDate

@Composable
fun ReferAndEarnMainUi(viewModel: ReferAndEarnViewModel, paddingValues: PaddingValues) {
    val userReferralDetailUi by viewModel.userReferralDetailUiState.collectAsState()
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(paddingValues)) {
        val (dataView, loadingView) = createRefs()
        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.screen_surface_color))
            .padding(horizontal = dp_16)
            .constrainAs(dataView) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
        {
            item {
                ProfileReferralSection(refer = viewModel.referCache,
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight())
            }
            when (val data = userReferralDetailUi) {
                is UserReferralDetailUiState.Success -> {
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
                                color = colorResource(id = R.color.new_material_primary), textAlign = TextAlign.End)
                        }
                    }

                    if (data.data.isEmpty()) {
                        item {
                            ViewSpacer12()
                        }
                        item {
                            Text(text = "You have not referred to anyone yet now. Please refer ZustApp and start Earning",
                                style = ZustTypography.bodyMedium,
                                color = colorResource(id = R.color.new_hint_color))
                        }
                    }
                    for ((level, details) in data.data) {
                        for (detail in details) {
                            item {
                                OutlinedCard(modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(vertical = dp_8),
                                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))) {
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
                }

                is UserReferralDetailUiState.Error -> {
                    item {
                        FullScreenErrorUi(errorCode = -1, retryCallback = { viewModel.getUserReferralDetails() }) {
                        }
                    }
                }

                is UserReferralDetailUiState.Initial -> {

                }
            }
        }
        if (userReferralDetailUi.isLoading) {
            CustomAnimatedProgressBar(modifier = Modifier
                .constrainAs(loadingView) {
                    top.linkTo(parent.top, dp_16)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}