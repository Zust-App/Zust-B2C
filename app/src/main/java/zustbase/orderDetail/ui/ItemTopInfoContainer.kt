package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.R.color
import zustbase.orderDetail.models.OrderDetailData
import zustbase.orderDetail.utils.PdfViewer
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.utility.moveToInAppWebPage
import `in`.opening.area.zustapp.webpage.InAppWebActivity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ItemTopInfoContainer(data: OrderDetailData?,intentSource:String?) {
    val context: Context = LocalContext.current
    if (data == null) {
        return
    }
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (titleText, invoice, subTitleText, invoiceDownloadIcon) = createRefs()

        Text(text = "Congratulation!",
            modifier = Modifier.constrainAs(titleText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(invoiceDownloadIcon.start, dp_12)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodyMedium,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
            color = colorResource(id = color.app_black))
        Icon(painter = painterResource(id = R.drawable.download_icon), contentDescription = "download",
            modifier = Modifier.constrainAs(invoiceDownloadIcon) {
                top.linkTo(invoice.top)
                bottom.linkTo(invoice.bottom)
                start.linkTo(titleText.end)
            })
        Text(text = "Download Bill",
            color = colorResource(id = color.new_material_primary),
            modifier = Modifier
                .padding(end = 12.dp)
                .constrainAs(invoice) {
                    top.linkTo(titleText.top)
                    bottom.linkTo(titleText.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(invoiceDownloadIcon.end, dp_6)
                }
                .clickable {
                    val inAppWebActivity = Intent(context, InAppWebActivity::class.java).apply {
                        putExtra(InAppWebActivity.WEB_URL, "")
                        putExtra(InAppWebActivity.TITLE_TEXT, "Invoice")
                        putExtra(INTENT_SOURCE,intentSource?: INTENT_SOURCE_GROCERY)
                        putExtra(ORDER_ID, data.orderId)
                    }
                    context.startActivity(inAppWebActivity)
                },
            style = ZustTypography.bodyMedium,
            fontSize = 12.sp)

        Text(text = "Your order has been successfully processed.",
            color = colorResource(id = color.black_3),
            style = ZustTypography.bodyMedium, modifier = Modifier
                .padding(end = 12.dp)
                .constrainAs(subTitleText) {
                    top.linkTo(titleText.bottom, dp_8)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
                .clickable {
                    val inAppWebActivity = Intent(context, InAppWebActivity::class.java)
                    inAppWebActivity.putExtra(InAppWebActivity.WEB_URL, "")
                    inAppWebActivity.putExtra(InAppWebActivity.TITLE_TEXT, "Invoice")
                    inAppWebActivity.putExtra(ORDER_ID, data.orderId)
                    context.startActivity(inAppWebActivity)
                })
    }
}
