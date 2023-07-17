package `in`.opening.area.zustapp.helpline

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun HelplineContainer(profileViewModel: ProfileViewModel?, supportBtmSheetCallback: (Any) -> Unit) {
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = colorResource(id = R.color.white))
        .padding(horizontal = 20.dp)) {
        val (closeIcon, helplineColumn) = createRefs()
        Icon(painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = stringResource(id = R.string.close), modifier = Modifier
                .clickable {
                    supportBtmSheetCallback.invoke("close")
                }
                .constrainAs(closeIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top, dp_16)
                })

        Column(modifier = Modifier.constrainAs(helplineColumn) {
            top.linkTo(closeIcon.top, dp_8)
            start.linkTo(parent.start, dp_16)
            end.linkTo(parent.end, dp_16)
            width = Dimension.fillToConstraints
        }) {
            Text(text = stringResource(R.string.help_and_support),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = ZustTypography.body1,
                fontSize = 18.sp,
                color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.contact_nmber),
                color = colorResource(id = R.color.green),
                style = ZustTypography.body2,
                fontSize = 14.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val phoneNos = profileViewModel?.getSupportCsDetail()?.phoneNos
            if (!phoneNos.isNullOrEmpty()) {
                phoneNos.forEach {
                    Text(
                        text = it, color = colorResource(id = R.color.light_black),
                        style = ZustTypography.body2,
                        fontSize = 14.sp, modifier = Modifier.clickable {
                            AppUtility.openCallIntent(context, it)
                        })
                }
            } else {
                Text(
                    text = "7858906229", color = colorResource(id = R.color.light_black),
                    style = ZustTypography.body2,
                    fontSize = 14.sp, modifier = Modifier.clickable {
                        AppUtility.openCallIntent(context, "7858906229")
                    })
                Text(
                    text = "7564062907", color = colorResource(id = R.color.light_black),
                    style = ZustTypography.body2,
                    fontSize = 14.sp, modifier = Modifier.clickable {
                        AppUtility.openCallIntent(context, "7564062907")
                    })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Email", color = colorResource(id = R.color.green),
                style = ZustTypography.body2,
                fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            val emails = profileViewModel?.getSupportCsDetail()?.email
            if (!emails.isNullOrEmpty()) {
                emails.forEach {
                    Text(text = it, color = colorResource(id = R.color.light_black),
                        style = ZustTypography.body2,
                        fontSize = 14.sp, modifier = Modifier.clickable {
                            AppUtility.openEmailIntent(context, it)
                        })
                    Spacer(modifier = Modifier.height(4.dp))
                }
            } else {
                Text(text = "zusttapp@gmail.com", color = colorResource(id = R.color.light_black),
                    style = ZustTypography.body2,
                    fontSize = 14.sp, modifier = Modifier.clickable {
                        AppUtility.openEmailIntent(context, "zusttapp@gmail.com")
                    })
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "contact@zustapp.com", color = colorResource(id = R.color.light_black),
                    style = ZustTypography.body2,
                    fontSize = 14.sp, modifier = Modifier.clickable {
                        AppUtility.openEmailIntent(context, "contact@zustapp.com")
                    })
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}