package `in`.opening.area.zustapp.orderSummary.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.viewmodels.DeliveryPartnerTipUiState
import `in`.opening.area.zustapp.viewmodels.OrderSummaryViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

val deliveryPartnerTipModifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp)

@Composable
fun DeliveryPartnerTipUi(orderSummaryViewModel: OrderSummaryViewModel) {
    val deliveryPartnerTipData by orderSummaryViewModel.deliveryPartnerTipUiState.collectAsState(initial = DeliveryPartnerTipUiState.InitialUi(false))

    val rupees = stringResource(id = R.string.ruppes)
    Column(modifier = Modifier.background(color = colorResource(id = R.color.white),
        shape = RoundedCornerShape(12.dp))) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            val (freshIcon, appIcon) = createRefs()
            Image(painter = painterResource(id = R.drawable.fresh_vegetable),
                contentDescription = "fresh", modifier = Modifier
                    .height(95.dp)
                    .fillMaxWidth()
                    .constrainAs(freshIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    })
            Image(painter = painterResource(id = R.drawable.zust_white_text),
                contentDescription = "fresh", modifier = Modifier
                    .width(95.dp)
                    .height(45.dp)
                    .constrainAs(appIcon) {
                        start.linkTo(parent.start, dp_20)
                        bottom.linkTo(parent.bottom, dp_20)
                    })
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tip to your partner", style = Typography_Montserrat.body1, color = colorResource(id = R.color.app_black), modifier = deliveryPartnerTipModifier)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "100% cancellation fee will be applicable if you decide to cancel th order anytime after order placement. Avoid cancellation as it leads to food wastage ", fontSize = 12.sp, color = colorResource(id = R.color.new_hint_color), fontFamily = montserrat, modifier = deliveryPartnerTipModifier, fontWeight = FontWeight.W500)

        Spacer(modifier = Modifier.height(16.dp))
        when (val data = deliveryPartnerTipData) {
            is DeliveryPartnerTipUiState.Success -> {
                Row(modifier = deliveryPartnerTipModifier) {
                    repeat(4) {
                        Card(modifier = Modifier
                            .weight(1f)
                            .height(40.dp), elevation = 4.dp) {
                            ConstraintLayout(modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(color = if (data.amountList[it] == data.selectedAmount) {
                                    colorResource(id = R.color.new_material_primary)
                                } else {
                                    colorResource(id = R.color.white)
                                })) {
                                val (deliveryTipText, emojiIcon) = createRefs()
                                Image(painter = getEmoji(it),
                                    contentDescription = "emoji", modifier = Modifier.constrainAs(emojiIcon) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start, dp_8)
                                    })

                                Text(text = rupees + data.amountList[it],
                                    style = Typography_Montserrat.body1,
                                    fontSize = 12.sp, modifier = Modifier
                                        .constrainAs(deliveryTipText) {
                                            top.linkTo(parent.top)
                                            bottom.linkTo(parent.bottom)
                                            start.linkTo(emojiIcon.end, dp_4)
                                            end.linkTo(parent.end)
                                        }
                                        .clickable {
                                            if (data.amountList[it] == data.selectedAmount) {
                                                orderSummaryViewModel.updateDeliveryPartnerTip(0)
                                            } else {
                                                orderSummaryViewModel.updateDeliveryPartnerTip(data.amountList[it])
                                            }
                                        },
                                    textAlign = TextAlign.Center,
                                    color = if (data.amountList[it] != data.selectedAmount) {
                                        colorResource(id = R.color.new_hint_color)
                                    } else {
                                        colorResource(id = R.color.white)
                                    })
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                }
            }
            else -> {}
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun getEmoji(index: Int): Painter {
    when (index) {
        0 -> return painterResource(id = R.drawable.emoji_1)
        1->return painterResource(id = R.drawable.emoji_2)
        2-> return painterResource(id = R.drawable.emoji_3)
    }
    return painterResource(id = R.drawable.emoji_4)
}