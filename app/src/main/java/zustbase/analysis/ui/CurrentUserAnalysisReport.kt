package zustbase.analysis.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import zustbase.analysis.models.UserReport
import zustbase.custom.pressClickEffect

var rectShape16p = RoundedCornerShape(dp_16)

@Composable
fun CurrentUserAnalysisReport(userReport: UserReport?) {
    if (userReport == null) {
        return
    }
    if (userReport.orderCount != null && userReport.expense != null && userReport.ranking != null) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dp_16, vertical = dp_8)) {
            Column(modifier = Modifier
                .shadow(elevation = dp_8, clip = false, shape = rectShape16p)
                .weight(1f)
                .border(border = BorderStroke(1.dp, color = colorResource(id = R.color.white)), shape = rectShape16p)
                .background(shape = RoundedCornerShape(dp_16),
                    color = colorResource(id = R.color.some_silver))
                .padding(vertical = dp_16), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = userReport.orderCount.toString(), style = ZustTypography.bodyMedium)
                Spacer(modifier = Modifier.height(dp_8))
                Text(text = "Order", style = ZustTypography.bodyMedium)
            }
            Spacer(modifier = Modifier.width(dp_16))
            Column(modifier = Modifier
                .shadow(elevation = dp_8, clip = false, shape = rectShape16p)
                .weight(1f)
                .border(border = BorderStroke(1.dp, color = colorResource(id = R.color.white)), shape = rectShape16p)
                .background(shape = rectShape16p,
                    color = colorResource(id = R.color.lighting))
                .padding(vertical = dp_16), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = buildString {
                    append(stringResource(id = R.string.ruppes))
                    append(userReport.expense.toInt())
                }, style = ZustTypography.bodyMedium)
                Spacer(modifier = Modifier.height(dp_8))
                Text(text = "Amount", style = ZustTypography.bodyMedium)
            }
            Spacer(modifier = Modifier.width(dp_16))
            Column(modifier = Modifier
                .shadow(elevation = dp_8, clip = false, shape = rectShape16p)
                .weight(1f)
                .border(border = BorderStroke(1.dp, color = colorResource(id = R.color.white)), shape = rectShape16p)
                .background(shape = rectShape16p,
                    color = colorResource(id = R.color.screen_surface_color))
                .padding(vertical = dp_16), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = buildString {
                    append("#")
                    append(userReport.ranking)
                }, style = ZustTypography.bodyMedium)
                Spacer(modifier = Modifier.height(dp_8))
                Text(text = "Rank", style = ZustTypography.bodyMedium)
            }
        }
    }
}