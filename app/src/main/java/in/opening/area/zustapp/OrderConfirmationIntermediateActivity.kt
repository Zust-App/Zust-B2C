package `in`.opening.area.zustapp

import `in`.opening.area.zustapp.R.anim
import `in`.opening.area.zustapp.R.color
import `in`.opening.area.zustapp.compose.ComposeLottieWithCallback
import `in`.opening.area.zustapp.orderDetail.OrderDetailActivity
import `in`.opening.area.zustapp.orderDetail.ui.PREFIX_ORDER_ID
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.zustFont
import `in`.opening.area.zustapp.ui.theme.okraFontFamily
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint

val maxHeightWidthModifier = Modifier
    .fillMaxWidth()
    .fillMaxHeight()

@AndroidEntryPoint
class OrderConfirmationIntermediateActivity : AppCompatActivity() {
    private var orderId: Int? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = intent.getIntExtra(OrderDetailActivity.ORDER_ID, -1)
        val completeOrderId = PREFIX_ORDER_ID + orderId
        setContent {
            MainUiContainer(completeOrderId)
        }
    }

    @Composable
    private fun MainUiContainer(completeOrderId: String) {
        Scaffold(modifier = maxHeightWidthModifier) { paddingValues ->
            ConstraintLayout(modifier = maxHeightWidthModifier
                .padding(paddingValues)) {
                val (successAnimation) = createRefs()
                val (thanksMessage) = createRefs()
                val (orderConfirmedMessage) = createRefs()
                Text(text = stringResource(`in`.opening.area.zustapp.R.string.order_confirmation_message, completeOrderId, 45),
                    modifier = Modifier.constrainAs(orderConfirmedMessage) {
                        bottom.linkTo(successAnimation.top, dp_16)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, fontWeight = FontWeight.W500,
                    fontFamily = okraFontFamily,
                    color = colorResource(id = color.light_green), textAlign = TextAlign.Center)

                Text(text = stringResource(`in`.opening.area.zustapp.R.string.thanks_for_using_zust_app),
                    modifier = Modifier.constrainAs(thanksMessage) {
                        top.linkTo(successAnimation.bottom, dp_16)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, fontWeight = FontWeight.W600,
                    fontFamily = zustFont,
                    color = colorResource(id = color.new_material_primary),
                    textAlign = TextAlign.Center)
                ComposeLottieWithCallback(rawId = `in`.opening.area.zustapp.R.raw.success, modifier = Modifier
                    .constrainAs(successAnimation) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(200.dp)
                    .padding(0.dp), speed = 1f) {
                    startOrderDetailPage(orderId)
                }
            }
        }
    }

    private fun startOrderDetailPage(orderId: Int?) {
        if (orderId == null) {
            return
        }
        val orderDetailIntent: Intent? by lazy { Intent(this, OrderDetailActivity::class.java) }
        orderDetailIntent?.putExtra(OrderDetailActivity.ORDER_ID, orderId)
        orderDetailIntent?.putExtra(OrderDetailActivity.JUST_ORDERED, true)
        orderDetailIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(orderDetailIntent)
        overridePendingTransition(anim.slide_in_right, anim.slide_out_left)
    }
}

