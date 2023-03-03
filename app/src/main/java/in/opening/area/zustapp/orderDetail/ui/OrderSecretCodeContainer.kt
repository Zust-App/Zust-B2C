package `in`.opening.area.zustapp.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderDetail.models.OrderDetailData
import `in`.opening.area.zustapp.ui.theme.*
import `in`.opening.area.zustapp.utility.AppUtility
import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun OrderSecretCodeContainer(data: OrderDetailData?) {
    if (data == null) {
        return
    }
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .background(color = colorResource(id = R.color.yellow),
            shape = RoundedCornerShape(8.dp))) {
        if (!data.secretCode.isNullOrEmpty()) {
            val (secretCodeTv, warningText) = createRefs()
            Text(text = Html.fromHtml("Secret code: <b>${data.secretCode}</b>").toString(),
                style = Typography_Montserrat.body1,
                modifier = Modifier
                    .constrainAs(secretCodeTv) {
                        end.linkTo(parent.end, dp_12)
                        top.linkTo(parent.top, dp_12)
                        start.linkTo(parent.start, dp_16)
                        width = Dimension.fillToConstraints
                    }
                    .clickable {
                        AppUtility.copyToClipboard(context, text = data.secretCode, "Secret code")
                    }, color = colorResource(id = R.color.new_material_primary))

            Text(text = stringResource(R.string.warning_secret_code),
                style = Typography_Montserrat.body2,
                modifier = Modifier.constrainAs(warningText) {
                    end.linkTo(parent.end, dp_12)
                    top.linkTo(secretCodeTv.bottom, dp_8)
                    start.linkTo(parent.start, dp_16)
                    bottom.linkTo(parent.bottom, dp_12)
                    width = Dimension.fillToConstraints
                }, color = colorResource(id = R.color.new_hint_color))
        }
    }
}