package `in`.opening.area.zustapp

import `in`.opening.area.zustapp.R.anim
import `in`.opening.area.zustapp.R.color
import `in`.opening.area.zustapp.compose.ComposeLottieWithCallback
import zustbase.orderDetail.OrderDetailActivity
import zustbase.orderDetail.ui.PREFIX_ORDER_ID_GROCERY
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.zustFont
import `in`.opening.area.zustapp.ui.theme.okraFontFamily
import `in`.opening.area.zustapp.utility.AppUtility
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
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
import zustbase.orderDetail.ui.INTENT_SOURCE
import zustbase.orderDetail.ui.INTENT_SOURCE_NON_VEG
import zustbase.orderDetail.ui.JUST_ORDERED
import zustbase.orderDetail.ui.ORDER_ID
import zustbase.orderDetail.ui.PREFIX_ORDER_ID_NON_VEG

val maxHeightWidthModifier = Modifier
    .fillMaxWidth()
    .fillMaxHeight()

@AndroidEntryPoint
class OrderConfirmationIntermediateActivity : AppCompatActivity() {
    private var orderId: Int? = -1
    private var intentSource: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = intent.getIntExtra(ORDER_ID, -1)
        if (intent.hasExtra(INTENT_SOURCE)) {
            intentSource = intent.getStringExtra(INTENT_SOURCE)
        }
        val completeOrderId = if (intentSource == INTENT_SOURCE_NON_VEG) {
            PREFIX_ORDER_ID_NON_VEG + orderId
        } else {
            PREFIX_ORDER_ID_GROCERY + orderId
        }
        setContent {
            MainUiContainer(completeOrderId)
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    @Composable
    private fun MainUiContainer(completeOrderId: String) {
        Scaffold(modifier = maxHeightWidthModifier) { paddingValues ->
            ConstraintLayout(modifier = maxHeightWidthModifier
                .padding(paddingValues)) {
                val (successAnimation) = createRefs()
                val (thanksMessage) = createRefs()
                val (orderConfirmedMessage) = createRefs()
                Text(text = stringResource(R.string.order_confirmation_message, completeOrderId, 2),
                    modifier = Modifier.constrainAs(orderConfirmedMessage) {
                        bottom.linkTo(successAnimation.top, dp_16)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, fontWeight = FontWeight.W500,
                    fontFamily = okraFontFamily,
                    color = colorResource(id = color.light_green), textAlign = TextAlign.Center)

                Text(text = stringResource(R.string.thanks_for_using_zust_app),
                    modifier = Modifier.constrainAs(thanksMessage) {
                        top.linkTo(successAnimation.bottom, dp_16)
                        start.linkTo(parent.start, dp_20)
                        end.linkTo(parent.end, dp_20)
                        width = Dimension.fillToConstraints
                    }, fontWeight = FontWeight.W600,
                    fontFamily = zustFont,
                    color = colorResource(id = color.new_material_primary),
                    textAlign = TextAlign.Center)
                ComposeLottieWithCallback(rawId = R.raw.success, modifier = Modifier
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
        orderDetailIntent?.putExtra(ORDER_ID, orderId)
        orderDetailIntent?.putExtra(JUST_ORDERED, true)
        orderDetailIntent?.putExtra(INTENT_SOURCE, intentSource)
        orderDetailIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(orderDetailIntent)
        overridePendingTransition(anim.slide_in_right, anim.slide_out_left)
    }

    private var onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            AppUtility.showToast(this@OrderConfirmationIntermediateActivity, "Please wait while")
        }
    }
}

