package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_8
import ui.colorWhite
import ui.colorWhite1

const val KEY_SUGGEST_PRODUCT = "suggest"
fun LazyListScope.homeSuggestProductUi(callback: (ACTION) -> Unit) {
    item(key = KEY_SUGGEST_PRODUCT) {
        Column(modifier = padding_16Modifier) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(text = "Oops, Didn't find what you\n" + "are looking for?", style = ZustTypography.bodyMedium,
                fontSize = 20.sp, color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(shape = RoundedCornerShape(dp_12),
                onClick = { callback.invoke(ACTION.SUGGEST_PRODUCT) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.white),
                    contentColor = colorResource(id = R.color.white)),
                modifier = Modifier.wrapContentHeight()
                    .clip(RoundedCornerShape(dp_12)),
            ) {
                Text(text = "Suggest items", style = ZustTypography.bodyMedium,
                    color = colorResource(id = R.color.new_material_primary))
            }
        }
    }
}