package `in`.opening.area.zustapp.orderDetail

import `in`.opening.area.zustapp.HomeLandingActivity
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.orderDetail.ui.OrderDetailsTopAppBar
import `in`.opening.area.zustapp.orderDetail.ui.OrderedDetailsContainer
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.constraintlayout.compose.ConstraintLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailActivity : ComponentActivity() {
    private val myOrdersListViewModel: MyOrdersListViewModel by viewModels()

    private var orderId: Int? = -1
    private var justOrdered: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            orderId = intent.getIntExtra(ORDER_ID, -1)
            if (intent.hasExtra(JUST_ORDERED)) {
                justOrdered = intent.getBooleanExtra(JUST_ORDERED, false)
            }
            Scaffold(
                backgroundColor = colorResource(id = R.color.screen_surface_color),
                topBar = {
                    OrderDetailsTopAppBar(modifier = Modifier, orderId = orderId) {
                        handleAction(it)
                    }
                }) {
                ConstraintLayout(modifier = Modifier
                    .padding(it)
                    .background(colorResource(id = R.color.screen_surface_color))
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    OrderedDetailsContainer(myOrdersListViewModel, this) {
                        justOrdered = true
                        handleAction(ACTION.NAV_BACK)
                    }
                }
            }

            LaunchedEffect(key1 = Unit, block = {
                if (orderId != -1) {
                    myOrdersListViewModel.getOrderDetails(orderId!!)
                }
            })
        }
    }

    private fun handleAction(it: ACTION) {
        if (it == ACTION.NAV_BACK) {
            if (justOrdered) {
                val intent = Intent(this, HomeLandingActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
                startActivity(intent)
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    companion object {
        const val JUST_ORDERED = "just_ordered"
        const val ORDER_ID = "order_id"
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (justOrdered) {
            val intent = Intent(this, HomeLandingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Removes other Activities from stack
            startActivity(intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}


