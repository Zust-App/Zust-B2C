package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.home.models.HomePageGenericData
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage

const val KEY_DELIVERY_ALERT = "delivery_alert"
fun LazyListScope.homePageDeliveryAlert(data: List<HomePageGenericData>, callback: (ACTION) -> Unit) {
    item(key = KEY_DELIVERY_ALERT) {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(130.dp)
            .background(color = colorResource(id = R.color.light_green))) {
            val verticalGuideline = createGuidelineFromStart(0.5f)
            val (alertTextTitle, alertTextImage, alertTextBody, waIcon, callUs) = createRefs()
            Text(text = "Delivery Alert",
                color = colorResource(id = R.color.white),
                style = Typography_Montserrat.body1,
                modifier = Modifier
                    .constrainAs(alertTextTitle) {
                        start.linkTo(parent.start, dp_16)
                        end.linkTo(parent.end, dp_12)
                        top.linkTo(parent.top, dp_16)
                        width = Dimension.fillToConstraints
                    })

            Image(painter = painterResource(id = R.drawable.handshake),
                contentDescription = "", modifier = Modifier
                    .constrainAs(alertTextImage) {
                        top.linkTo(alertTextTitle.bottom, dp_12)
                        start.linkTo(parent.start, dp_16)
                        bottom.linkTo(parent.bottom, dp_12)
                        end.linkTo(verticalGuideline)
                    }
                    .size(100.dp))

            Text(text = data[0].description ?: "For now you can call or whatsapp us",
                color = colorResource(id = R.color.white),
                style = Typography_Montserrat.body2,
                modifier = Modifier
                    .wrapContentHeight()
                    .constrainAs(alertTextBody) {
                        top.linkTo(alertTextTitle.bottom, dp_8)
                        end.linkTo(parent.end)
                        start.linkTo(verticalGuideline)
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
                        end.linkTo(waIcon.start, dp_12)
                        bottom.linkTo(parent.bottom, dp_12)
                    }
                    .clickable {
                        callback.invoke(ACTION.PHONE_CALL)
                    }
            )
        }
    }
}