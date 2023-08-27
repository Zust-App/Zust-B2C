package zustbase.analysis.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_32
import `in`.opening.area.zustapp.ui.theme.dp_8

@Composable
fun TopGainerSingleItemUi(name: String, expense: Double, rank: Int) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = dp_16, vertical = dp_8), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(dp_32)
                .background(color = colorResource(id = R.color.screen_surface_color), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = rank.toString(), color = colorResource(id = R.color.black_3), style = ZustTypography.bodyMedium)
        }
        Spacer(modifier = Modifier.width(dp_12))
        Text(text = name, style = ZustTypography.bodyMedium, color = colorResource(id = R.color.black_2))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = buildString {
            append(stringResource(id = R.string.ruppes))
            append(expense.toInt())
        }, style = ZustTypography.bodyMedium, color = colorResource(id = R.color.new_hint_color))
    }
}