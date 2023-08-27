package zustbase.orderDetail.ui

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExploreMoreUi(clickCallback: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()) {
        Button(onClick = {
            clickCallback.invoke()
        }, colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.new_material_primary))) {
            Text(text = "Continue shopping at",
                style = ZustTypography.bodyMedium,
                color = colorResource(id = R.color.white))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.app_name),
                style = ZustTypography.bodyMedium,
                fontWeight = FontWeight.W700,
                color = colorResource(id = R.color.white))
        }
    }
}