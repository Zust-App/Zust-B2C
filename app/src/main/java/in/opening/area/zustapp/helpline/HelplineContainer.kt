package `in`.opening.area.zustapp.helpline

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun HelplineContainer(profileViewModel: ProfileViewModel, supportBtmSheetCallback: (Any) -> Unit) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
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
                style = Typography_Montserrat.body1,
                fontSize = 18.sp,
                color = colorResource(id = R.color.app_black))

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.contact_nmber),
                color = colorResource(id = R.color.black_2),
                style = Typography_Montserrat.body2,
                fontSize = 16.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))

            profileViewModel.getSupportCsDetail()?.phoneNos?.forEach {
                Text(
                    text = it, color = colorResource(id = R.color.black_3),
                    style = Typography_Montserrat.body2,
                    fontSize = 14.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Email", color = colorResource(id = R.color.black_2),
                style = Typography_Montserrat.body2,
                fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))

            profileViewModel.getSupportCsDetail()?.email?.forEach {
                Text(text = it, color = colorResource(id = R.color.black_3),
                    style = Typography_Montserrat.body2,
                    fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}