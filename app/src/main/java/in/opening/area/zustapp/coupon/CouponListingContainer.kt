package `in`.opening.area.zustapp.coupon

import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.uiModels.CouponListUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.CouponListingViewModel
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun CouponListingContainer(viewModel: CouponListingViewModel,
                           modifier: Modifier,couponItemClickListener: CouponItemClickListener) {
    val couponResponse = viewModel.couponListUiState.collectAsState(initial = CouponListUi.InitialUi(isLoading = true))
    val context: Context = LocalContext.current

    val response = couponResponse.value
    when (response) {
        is CouponListUi.CouponSuccess -> {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier.fillMaxWidth()
                    .padding(horizontal = 12.dp)) {
                items(response.data) { item ->
                    CouponItemView(item, Modifier,couponItemClickListener)
                }
            }
        }
        is CouponListUi.InitialUi -> {
            if (response.isLoading) {
                ShowProgressBar()
            }
        }
        is CouponListUi.ErrorUi -> {
            if (!response.errorMsg.isNullOrEmpty()) {
                AppUtility.showToast(context, response.errorMsg)
            } else {
                AppUtility.showToast(context, response.errors.getTextMsg())
            }
        }

    }
    if (response.isLoading) {
        ShowProgressBar()
    }
}

@Composable
private fun ShowProgressBar() {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 100.dp)
        .height(100.dp)) {
        val (pgBar) = createRefs()
        CircularProgressIndicator(
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
                .constrainAs(pgBar) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}