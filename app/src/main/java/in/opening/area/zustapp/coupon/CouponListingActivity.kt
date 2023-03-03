package `in`.opening.area.zustapp.coupon

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.coupon.model.Coupon
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.viewmodels.CouponListingViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CouponListingActivity : ComponentActivity(), CouponItemClickListener {
    private val couponViewModel: CouponListingViewModel by viewModels()

    companion object {
        const val INTENT_KEY_COUPON_VALUE = "coupon_value"
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = "Coupon",
                    callback = {
                        if (it == ACTION.NAV_BACK) {
                            finish()
                        }
                    })
            }, modifier = Modifier.background(screenBgColor)) {
                LoadMainContent(it)
            }
        }
        couponViewModel.getCouponListFromServer()
    }

    @Composable
    fun LoadMainContent(paddingValues: PaddingValues) {
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(color = screenBgColor)) {
            val (manualCouponContainer, titleText, couponList) = createRefs()
            ConstraintLayout(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(manualCouponContainer) {
                    top.linkTo(parent.top, dp_16)
                    start.linkTo(parent.start, dp_16)
                    end.linkTo(parent.end, dp_16)
                    width = Dimension.fillToConstraints
                }
                .wrapContentHeight()
                .border(width = 1.dp, color = Color(0xffB58AFB), shape = RoundedCornerShape(8.dp))
                .background(color = Color.White)) {
                val (manualText, apply) = createRefs()
                TextField(value = couponViewModel.userInputCoupon, onValueChange = {
                    couponViewModel.userInputCoupon = it
                }, colors = TextFieldDefaults.textFieldColors(
                    textColor = colorResource(id = R.color.grey_color_2),
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ), modifier = Modifier.constrainAs(manualText) {
                    start.linkTo(parent.start, dp_16)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }, placeholder = {
                    Text(text = "Enter Coupon here",
                        style = Typography_Montserrat.body2,
                        color = Color(0xffB2B2B2))
                }, singleLine = true)

                Text(text = "Apply", textAlign = TextAlign.End,
                    modifier = Modifier
                        .constrainAs(apply) {
                            start.linkTo(manualText.end, dp_16)
                            end.linkTo(parent.end, dp_16)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickable {
                            val intent = Intent()
                            intent.putExtra(INTENT_KEY_COUPON_VALUE, couponViewModel.userInputCoupon)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }, style = Typography_Montserrat.subtitle1,
                    color = colorResource(id = R.color.app_black))
            }
            Text(text = "Available Coupons", style = Typography_Montserrat.body1,
                modifier = Modifier.constrainAs(titleText) {
                    top.linkTo(manualCouponContainer.bottom, dp_24)
                    start.linkTo(parent.start, dp_16)
                    end.linkTo(parent.end, dp_16)
                    width = Dimension.fillToConstraints
                })
            CouponListingContainer(couponViewModel,
                modifier = Modifier.constrainAs(couponList) {
                    top.linkTo(titleText.bottom, dp_16)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }, this@CouponListingActivity)
        }
    }


    override fun didTapOnApply(item: Coupon) {
        val intent = Intent()
        intent.putExtra(INTENT_KEY_COUPON_VALUE, item.couponCode)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}
