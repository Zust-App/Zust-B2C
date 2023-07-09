package zustElectronics.zeLanding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.orderSummary.compose.OrderSummaryAction

@Composable
fun ZeHomeTrendingVerticalItems() {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        val (thumbnail, title, price) = createRefs()

        Image(painter = painterResource(id = R.drawable.home_icon),
            contentDescription = "electronics image",
            modifier = Modifier.constrainAs(thumbnail) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            })
        Text(text = "Product Title", modifier = Modifier)

        Text(text = "LG AI Convertible...", modifier = Modifier)

        Text(text = "Start From ₹20000", modifier = Modifier)

        Icon(painter = painterResource(id = R.drawable.arrow_right_icon),
            contentDescription = "")
    }
}
