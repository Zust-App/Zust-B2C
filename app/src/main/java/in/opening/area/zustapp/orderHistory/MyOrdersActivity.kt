package `in`.opening.area.zustapp.orderHistory

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.orderHistory.ui.BookingHistoryList
import `in`.opening.area.zustapp.viewmodels.MyOrdersListViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyOrdersActivity : ComponentActivity() {
    private val orderListViewModel: MyOrdersListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = "My Orders", null, null) {
                    handleAction(it)
                }
            }, content = {
                BookingHistoryList(viewModel = orderListViewModel, it)
            }, backgroundColor = colorResource(id = R.color.screen_surface_color))
        }
    }

    private fun handleAction(action: ACTION) {
        if (action == ACTION.NAV_BACK) {
            finish()
        }
    }
}
