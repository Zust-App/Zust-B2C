package `in`.opening.area.zustapp.refer.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.models.Refer
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileReferralSection(refer: Refer?, modifier: Modifier) {
    if (refer?.code == null) {
        return
    }
    val stroke = Stroke(width = 1f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    val context = LocalContext.current
    Column(modifier = modifier
        .padding(start = 16.dp, end = 16.dp, top = 12.dp)
        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(8.dp))
        .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(painter = painterResource(id = R.drawable.refer_coin),
            contentDescription = null, modifier = Modifier.size(90.dp))

        Text(text = refer.title ?: ("Save upto " + stringResource(id = R.string.ruppes) + "100"),
            fontSize = 20.sp,
            style = ZustTypography.body2,
            color = colorResource(id = R.color.app_black))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = refer.description!!,
            fontSize = 16.sp, style = ZustTypography.body2,
            color = Color(0x991E1E1E))

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(4.dp))
            .drawBehind {
                drawRoundRect(color = Color(0xff6750A4),
                    style = stroke)
            }
            .fillMaxWidth()
            .padding(12.dp)) {
            Text(text = refer.code, style = ZustTypography.body2,
                color = colorResource(id = R.color.app_black))
            Spacer(Modifier.weight(1f))

            Icon(painter = painterResource(id = R.drawable.ic_round_content_copy_24),
                contentDescription = null, modifier = Modifier.clickable {
                AppUtility.copyToClipboard(context, refer.code, "referral code")
            })
            Text(text = stringResource(R.string.copy_code), style = ZustTypography.body2,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        AppUtility.copyToClipboard(context, refer.code, "referral code")
                    }, color = colorResource(id = R.color.grey_color_2))
        }
        Spacer(modifier = Modifier.height(dp_16))
        Button(onClick = {
            val getText = AppUtility.getSharableTextOfReferralCode(refer.code)
            AppUtility.showShareIntent(context, getText)
        }, modifier = Modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(8.dp), color = colorResource(id = R.color.new_material_primary)), content = {
            Row() {
                Icon(painter = painterResource(id = R.drawable.share_icon),
                    contentDescription = "share", tint = colorResource(id = R.color.white))
                Spacer(modifier = Modifier.width(dp_8))
                Text(text = "Share", style = ZustTypography.body1, color = colorResource(id = R.color.white))
            }
        }, colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.new_material_primary)))
    }
}