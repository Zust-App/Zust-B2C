package `in`.opening.area.zustapp.profile.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.models.User
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

val profilePaddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)

@Composable
fun DisplayUserInfo(user: User?) {
    Column(modifier = Modifier.background(color = Color(0xfff5f6f9))) {
        ConstraintLayout(modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
            .wrapContentHeight()
            .background(color = Color(0xfffffefe),
                shape = RoundedCornerShape(8.dp))
            .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 16.dp)
            .fillMaxWidth()) {
            val (userIcon, titleText, phoneNum, affiliateTag, _) = createRefs()
            val displayText = user?.getDisplayIconText() ?: "G"
            Text(
                modifier = Modifier
                    .padding(4.dp)
                    .drawBehind {
                        drawCircle(
                            color = Color(0xffe26139),
                            radius = this.size.maxDimension
                        )
                    }
                    .padding(4.dp)
                    .constrainAs(userIcon) {
                        start.linkTo(parent.start, dp_8)
                        top.linkTo(titleText.top)
                        bottom.linkTo(phoneNum.bottom)
                    },
                text = displayText, color = Color.White,
                style = ZustTypography.bodyMedium
            )

            Text(text = user?.userName ?: stringResource(R.string.my_account),
                style = ZustTypography.bodyMedium,
                modifier = Modifier
                    .padding()
                    .constrainAs(titleText) {
                        top.linkTo(parent.top, dp_8)
                        start.linkTo(userIcon.end, 32.dp)
                        bottom.linkTo(phoneNum.top, dp_4)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }, color = colorResource(id = R.color.app_black))
            if (!user?.phoneNo.isNullOrEmpty()) {
                Text(text = "+91 " + user?.phoneNo,
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier
                        .padding()
                        .constrainAs(phoneNum) {
                            top.linkTo(titleText.bottom)
                            start.linkTo(userIcon.end, 32.dp)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, color = colorResource(id = R.color.black_3))
            }
            user?.affiliatePartner?.let {
                Text(text = it,
                    style = ZustTypography.bodyMedium,
                    modifier = Modifier
                        .padding()
                        .constrainAs(affiliateTag) {
                            top.linkTo(phoneNum.bottom)
                            start.linkTo(userIcon.end, 32.dp)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, color = colorResource(id = R.color.black_3))
            }
        }
    }

}
