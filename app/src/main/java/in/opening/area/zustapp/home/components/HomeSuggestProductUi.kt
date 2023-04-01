package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val KEY_SUGGEST_PRODUCT = "suggest"
fun LazyListScope.homeSuggestProductUi(callback: (ACTION) -> Unit) {
    item(key = KEY_SUGGEST_PRODUCT) {
        Column(modifier = padding_16Modifier) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(text = "Oops, Didn't find were you\n" +
                    "are looking for?", style = Typography_Montserrat.body1,
                fontSize = 20.sp, color = colorResource(id = R.color.app_black))
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { callback.invoke(ACTION.SUGGEST_PRODUCT) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(id = R.color.white),
                    contentColor = colorResource(id = R.color.white)),
                modifier = Modifier
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                Text(text = "Suggest items", style = Typography_Montserrat.body1,
                    color = colorResource(id = R.color.new_material_primary))
            }
        }
    }
}