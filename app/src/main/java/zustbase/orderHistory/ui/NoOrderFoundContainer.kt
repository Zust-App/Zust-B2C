package zustbase.orderHistory.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.compose.ComposeLottie
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.utility.proceedToHomePage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun NoOrderFoundContainer() {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(text = "Oops, No order placed yet",
            color = colorResource(id = R.color.light_black),
            style = ZustTypography.bodyMedium)
        ComposeLottie(rawId = R.raw.no_order1,
            modifier = Modifier
                .size(200.dp)
                .padding(0.dp), speed = 1f)
        Button(onClick = {
            context.proceedToHomePage()
        }, modifier = Modifier
            .clip(RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
            Text(text = "Order now",
                style = ZustTypography.bodyMedium,
                color = colorResource(id = R.color.white))
        }
    }
}