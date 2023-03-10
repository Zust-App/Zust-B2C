package `in`.opening.area.zustapp.profile.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.models.Refer
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun ProfileReferralSection(refer: Refer?) {
    if (refer?.code == null) {
        return
    }
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 12.dp)
        .wrapContentHeight()
        .background(color = Color(0xffFFE7FF),
            shape = RoundedCornerShape(8.dp))
        .padding(12.dp)) {
        Row {
            Icon(painter = painterResource(id = R.drawable.ic_outline_person_24),
                contentDescription = null, modifier = Modifier.size(50.dp))
            Column(modifier = Modifier.padding(bottom = 12.dp, start = 12.dp, end = 12.dp)) {
                Text(text = stringResource(R.string.refer_a_friend), fontSize = 16.sp,
                    style = Typography_Montserrat.body2,
                    color = colorResource(id = R.color.app_black))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = refer.description!!,
                    fontSize = 12.sp, style = Typography_Montserrat.body2,
                    color = colorResource(id = R.color.grey_color_2))
            }
        }
        Divider(modifier = Modifier
            .height(1.dp)
            .background(color = Color(0xffD8D8D8)))
        Text(text = stringResource(R.string.share_your_code), fontSize = 16.sp, style = Typography_Montserrat.body2,
            modifier = Modifier.padding(top = 8.dp), color = colorResource(id = R.color.purple))
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (couponCode, share) = createRefs()
            Row(modifier = Modifier
                .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .constrainAs(couponCode) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, dp_12)
                    bottom.linkTo(parent.bottom, dp_12)
                    end.linkTo(share.start, dp_12)
                    width = Dimension.fillToConstraints
                }) {
                Text(text = refer.code, style = Typography_Montserrat.body2, color = colorResource(id = R.color.app_black))
                Spacer(Modifier.weight(1f))
                Text(text = stringResource(R.string.copy_code), style = Typography_Montserrat.body2,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable {
                            AppUtility.copyToClipboard(context, refer.code, "referral code")
                        }, color = colorResource(id = R.color.grey_color_2))
                Icon(painter = painterResource(id = R.drawable.ic_round_content_copy_24), contentDescription = null, modifier = Modifier.clickable {
                    AppUtility.copyToClipboard(context, refer.code, "referral code")
                })
            }
            Icon(painter = painterResource(id = R.drawable.share_icon),
                contentDescription = stringResource(R.string.share), modifier = Modifier
                    .constrainAs(share) {
                        start.linkTo(couponCode.end, dp_12)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top, dp_12)
                    }
                    .clickable {
                        val getText = AppUtility.getSharableTextOfReferralCode(refer.code)
                        AppUtility.showShareIntent(context, getText)
                    }
                    .size(30.dp))
        }
    }
}