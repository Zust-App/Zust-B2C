package `in`.opening.area.zustapp.coupon

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.coupon.model.Coupon
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_24
import `in`.opening.area.zustapp.viewmodels.CouponListingViewModel
import ui.colorBlack
import ui.colorWhite

@AndroidEntryPoint
class CouponListingActivity : ComponentActivity(), CouponItemClickListener {
    private val couponViewModel: CouponListingViewModel by viewModels()

    companion object {
        const val INTENT_KEY_COUPON_VALUE = "coupon_value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier.background(color = colorWhite), titleText = "Coupon",
                    color = colorBlack,
                    callback = {
                        if (it == ACTION.NAV_BACK) {
                            finish()
                        }
                    })
            }, modifier = Modifier.background(color = colorResource(id = R.color.screen_surface_color))) {
                LoadMainContent(it)
            }
        }
        couponViewModel.getCouponListFromServer()
    }

    @Composable
    fun LoadMainContent(paddingValues: PaddingValues) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = colorResource(id = R.color.screen_surface_color))
        ) {
            val (manualCouponContainer, titleText, couponList) = createRefs()
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(manualCouponContainer) {
                        top.linkTo(parent.top, dp_16)
                        start.linkTo(parent.start, dp_16)
                        end.linkTo(parent.end, dp_16)
                        width = Dimension.fillToConstraints
                    }
                    .wrapContentHeight()
                    .border(width = 1.dp, color = Color(0xffB58AFB), shape = RoundedCornerShape(8.dp))
                    .background(color = Color.White)
            ) {
                Row(
                    Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = couponViewModel.userInputCoupon,
                        onValueChange = { couponViewModel.userInputCoupon = it },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = colorResource(id = R.color.grey_color_2),
                            disabledTextColor = Color.Transparent,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                text = "Enter Coupon here",
                                style = ZustTypography.bodyMedium,
                                color = Color(0xffB2B2B2)
                            )
                        },
                        singleLine = true
                    )
                    Text(
                        text = "Apply", textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 12.dp)
                            .clickable {
                                val intent = Intent()
                                intent.putExtra(INTENT_KEY_COUPON_VALUE, couponViewModel.userInputCoupon)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            },
                        style = ZustTypography.bodyMedium,
                        color = colorResource(id = R.color.app_black)
                    )
                }
            }
            Text(
                text = "Available Coupons",
                style = ZustTypography.bodyMedium,
                modifier = Modifier.constrainAs(titleText) {
                    top.linkTo(manualCouponContainer.bottom, dp_24)
                    start.linkTo(parent.start, dp_16)
                    end.linkTo(parent.end, dp_16)
                    width = Dimension.fillToConstraints
                }
            )
            CouponListingContainer(
                couponViewModel,
                modifier = Modifier.constrainAs(couponList) {
                    top.linkTo(titleText.bottom, dp_16)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                },
                this@CouponListingActivity
            )
        }
    }

    override fun didTapOnApply(item: Coupon) {
        val intent = Intent()
        intent.putExtra(INTENT_KEY_COUPON_VALUE, item.couponCode)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
