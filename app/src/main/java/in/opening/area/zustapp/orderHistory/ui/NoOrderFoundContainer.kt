package `in`.opening.area.zustapp.orderHistory.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource

@Composable
fun NoOrderFoundContainer() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "No order Found",
            color = colorResource(id = R.color.light_black),
            style = Typography_Montserrat.body2)
    }
}