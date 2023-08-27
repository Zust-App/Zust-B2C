package zustbase.orderDetail

import zustbase.ZustLandingActivity
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import zustbase.orderDetail.ui.OrderDetailsTopAppBar
import zustbase.orderDetail.ui.OrderedDetailsContainer
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import dagger.hilt.android.AndroidEntryPoint
import ui.colorBlack
import ui.colorWhite
import zustbase.orderDetail.ui.INTENT_SOURCE
import zustbase.orderDetail.ui.JUST_ORDERED
import zustbase.orderDetail.ui.ORDER_ID

@AndroidEntryPoint
class OrderDetailActivity : ComponentActivity() {
    private val myOrdersListViewModel: MyOrdersListViewModel by viewModels()

    private var orderId: Int? = -1
    private var justOrdered: Boolean = false
    private var intentSource: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            orderId = intent.getIntExtra(ORDER_ID, -1)
            myOrdersListViewModel.orderIdCache = orderId
            if (intent.hasExtra(JUST_ORDERED)) {
                justOrdered = intent.getBooleanExtra(JUST_ORDERED, false)
            }
            if (intent.hasExtra(INTENT_SOURCE)) {
                intentSource = intent.getStringExtra(INTENT_SOURCE)
            }
            myOrdersListViewModel.intentSource = intentSource
            Scaffold(
                containerColor = colorResource(id = R.color.screen_surface_color),
                topBar = {
                    OrderDetailsTopAppBar(modifier = Modifier
                        .background(color = colorWhite),
                        orderId = orderId, color = colorBlack, intentSource = intentSource) {
                        handleAction(it)
                    }
                }) {
                ConstraintLayout(
                    modifier = Modifier
                        .padding(it)
                        .background(colorResource(id = R.color.screen_surface_color))
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    OrderedDetailsContainer(myOrdersListViewModel, this) {
                        justOrdered = true
                        handleAction(ACTION.NAV_BACK)
                    }
                }
            }

            LaunchedEffect(key1 = Unit, block = {
                if (orderId != -1) {
                    myOrdersListViewModel.getOrderDetails(orderId!!, intentSource)
                }
            })
        }
    }

    private fun handleAction(it: ACTION) {
        if (it == ACTION.NAV_BACK) {
            if (justOrdered) {
                val intent = Intent(this, ZustLandingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
                startActivity(intent)
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (justOrdered) {
            val intent = Intent(this, ZustLandingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}


