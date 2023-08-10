package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_14
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.utility.AppUtility
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun HelpAndSupportUi() {
    val context: Context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.white),
            shape = RoundedCornerShape(8.dp))
        .clickable {
            openWhatsAppOrderIntent(context)
        }) {
        val (chatIcon, chatText, nextIcon) = createRefs()

        Icon(painter = painterResource(id = R.drawable.ic_round_chat_24),
            contentDescription = "next", modifier = Modifier
                .size(20.dp)
                .constrainAs(chatIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, dp_16)
                    bottom.linkTo(parent.bottom)
                })

        Text(
            text = "Need help? Chat with us",
            color = colorResource(id = R.color.app_black),
            modifier = Modifier
                .padding(end = 12.dp)
                .constrainAs(chatText) {
                    top.linkTo(parent.top, dp_14)
                    bottom.linkTo(parent.bottom, dp_14)
                    end.linkTo(nextIcon.start)
                    start.linkTo(chatIcon.end, dp_12)
                    width = Dimension.fillToConstraints
                },
            style = ZustTypography.body1,
        )

        Icon(painter = painterResource(id = R.drawable.arrow_right_icon),
            contentDescription = "next", modifier = Modifier
                .size(20.dp)
                .constrainAs(nextIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, dp_12)
                    start.linkTo(chatText.end, dp_12)
                })
    }
}

private fun openWhatsAppOrderIntent(context: Context?) {
    if (context == null) {
        return
    }
    val sendIntent = Intent(Intent.ACTION_VIEW)
    sendIntent.data = Uri.parse(AppUtility.getWhatsappHelpUrl())
    if (AppUtility.isAppInstalled(AppUtility.WA_PACKAGE_NAME)) {
        context.startActivity(sendIntent)
    } else if (AppUtility.isAppInstalled(AppUtility.BUSINESS_WA_PACKAGE_NAME)) {
        context.startActivity(sendIntent)
    } else {
        AppUtility.showToast(context, "Whatsapp not installed")
    }
}
