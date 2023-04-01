package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.home.ACTION
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

const val KEY_ORDER_VIA_WA = "wa"
fun LazyListScope.orderViaWhatsappUi(callback: (ACTION) -> Unit) {
    item(key = KEY_ORDER_VIA_WA) {
        Column(modifier = padding_16Modifier) {
            Spacer(modifier = Modifier.height(42.dp))
            Image(painter = painterResource(id = R.drawable.order_via_whatsapp),
                contentDescription = "order via whatsapp", modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        callback.invoke(ACTION.ORDER_WA)
                    })
        }
    }

}
