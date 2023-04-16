package `in`.opening.area.zustapp.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.models.RiderDetails
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun DeliveryPartnerInfoUi(riderDetails: RiderDetails) {
    if (!riderDetails.riderName.isNullOrEmpty()) {
        val context = LocalContext.current
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(dp_8))) {
            val (text, icon) = createRefs()
            Text(text = riderDetails.riderName + " is your Delivery Partner",
                style = ZustTypography.body2,
                color = colorResource(id = R.color.app_black),
                modifier = Modifier
                    .wrapContentHeight()
                    .constrainAs(text) {
                        top.linkTo(parent.top, dp_12)
                        start.linkTo(parent.start, dp_16)
                        end.linkTo(icon.start, dp_16)
                        bottom.linkTo(parent.bottom, dp_12)
                        width = Dimension.fillToConstraints
                    })
            Image(painter = painterResource(id = R.drawable.call_icon),
                contentDescription = "call", modifier = Modifier
                    .constrainAs(icon) {
                        end.linkTo(parent.end, dp_16)
                        top.linkTo(parent.top, dp_12)
                    }
                    .clickable {
                        if (!riderDetails.riderPhone.isNullOrEmpty()) {
                            AppUtility.openCallIntent(context, riderDetails.riderPhone)
                        } else {
                            AppUtility.showToast(context, "Please connect on whatsapp")
                        }
                    })
        }
    }
}