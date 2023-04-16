package `in`.opening.area.zustapp.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.R.color
import `in`.opening.area.zustapp.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.orderDetail.utils.PdfViewer
import `in`.opening.area.zustapp.ui.theme.*
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
fun ItemTopInfoContainer(data: OrderDetailData?) {
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
            }, style = ZustTypography.body1,
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
                    PdfViewer.openPdfUsingOrderId("", context)
                },
            style = ZustTypography.body1,
            fontSize = 12.sp)

        Text(text = "Your order has been successfully processed.",
            color = colorResource(id = color.black_3),
            style = ZustTypography.body2, modifier = Modifier
                .padding(end = 12.dp)
                .constrainAs(subTitleText) {
                    top.linkTo(titleText.bottom, dp_8)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
                .clickable {
                    PdfViewer.openPdfUsingOrderId("", context)
                })
    }
}
