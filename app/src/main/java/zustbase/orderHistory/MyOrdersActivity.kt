package zustbase.orderHistory

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update
import zustbase.orderHistory.ui.UserGroceryBookingList
import zustbase.orderHistory.ui.UserNonVegBookingList
import zustbase.ui.ComposeCustomOrderHistoryTopBar

@AndroidEntryPoint
class MyOrdersActivity : ComponentActivity() {
    private val orderListViewModel: MyOrdersListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(topBar = {
                ComposeCustomOrderHistoryTopBar(modifier = Modifier, titleText = "My Orders", null, null) {
                    handleAction(it)
                }
            }, content = {
                BookingHistoryContainer(it)
            }, backgroundColor = colorResource(id = R.color.screen_surface_color))
        }
    }

    private fun handleAction(action: ACTION) {
        if (action == ACTION.NAV_BACK) {
            finish()
        }
        if (action == ACTION.NON_VEG_TAB) {
            orderListViewModel.orderHistoryTab.update {
                ACTION.NON_VEG_TAB
            }
        }
        if (action == ACTION.GROCERY_TAB) {
            orderListViewModel.orderHistoryTab.update {
                ACTION.GROCERY_TAB
            }
        }
    }

    @Composable
    private fun BookingHistoryContainer(paddingValues: PaddingValues) {
        val tab = orderListViewModel.orderHistoryTab.collectAsState()
        if (tab.value == ACTION.GROCERY_TAB) {
            UserGroceryBookingList(paddingValues = paddingValues)
        }
        if (tab.value == ACTION.NON_VEG_TAB) {
            UserNonVegBookingList(paddingValues = paddingValues)
        }
    }
}

