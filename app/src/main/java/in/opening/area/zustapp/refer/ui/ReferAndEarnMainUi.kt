package `in`.opening.area.zustapp.refer.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.refer.ReferAndEarnViewModel
import `in`.opening.area.zustapp.ui.theme.dp_16
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ReferAndEarnMainUi(viewModel: ReferAndEarnViewModel, paddingValues: PaddingValues) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(color = colorResource(id = R.color.screen_surface_color))
        .padding(paddingValues)) {
        val (handShakeImage) = createRefs()
        val (referralContainer) = createRefs()

        Image(painter = painterResource(id = R.drawable.handshake),
            contentDescription = "handshake",
            modifier = Modifier
                .constrainAs(handShakeImage) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(referralContainer.top)
                    height = Dimension.fillToConstraints
                })

        ProfileReferralSection(refer = viewModel.referCache,
            Modifier
                .constrainAs(referralContainer) {
                    top.linkTo(parent.top, dp_16)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, dp_16)
                }
                .fillMaxWidth()
                .wrapContentHeight())
    }
}