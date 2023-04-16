package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

const val KEY_DELIVERY_ALERT = "delivery_alert"
fun LazyListScope.homePageDeliveryAlert(data: List<HomePageGenericData>, callback: (ACTION) -> Unit) {
    item(key = KEY_DELIVERY_ALERT) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .wrapContentHeight()
            .background(color = colorResource(id = R.color.light_green))) {
            val (alertTextTitle, alertTextBody, waIcon, callUs) = createRefs()
            Text(text = "Delivery Alert",
                color = colorResource(id = R.color.white),
                style = ZustTypography.body1,
                modifier = Modifier
                    .constrainAs(alertTextTitle) {
                        start.linkTo(parent.start, dp_16)
                        end.linkTo(parent.end, dp_12)
                        top.linkTo(parent.top, dp_16)
                        width = Dimension.fillToConstraints
                    })


            Text(text = data[0].description ?: "For now you can call or whatsapp us",
                color = colorResource(id = R.color.white),
                style = ZustTypography.body2,
                modifier = Modifier
                    .wrapContentHeight()
                    .constrainAs(alertTextBody) {
                        top.linkTo(alertTextTitle.bottom, dp_8)
                        end.linkTo(parent.end, dp_20)
                        start.linkTo(parent.start, dp_16)
                        bottom.linkTo(waIcon.top, dp_12)
                        width = Dimension.fillToConstraints
                    }, overflow = TextOverflow.Ellipsis)

            Image(
                painter = painterResource(id = R.drawable.whatsapp_icon),
                contentDescription = "whatsapp now",
                modifier = Modifier
                    .size(30.dp)
                    .constrainAs(waIcon) {
                        end.linkTo(parent.end, dp_12)
                        bottom.linkTo(parent.bottom, dp_12)
                    }
                    .clickable {
                        callback.invoke(ACTION.ORDER_WA)
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.call_icon),
                contentDescription = "whatsapp now",
                modifier = Modifier
                    .size(30.dp)
                    .constrainAs(callUs) {
                        end.linkTo(waIcon.start, dp_20)
                        bottom.linkTo(parent.bottom, dp_12)
                    }
                    .clickable {
                        callback.invoke(ACTION.PHONE_CALL)
                    }
            )
        }
    }
}