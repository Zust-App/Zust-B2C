package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

const val KEY_BRAND = "brand_tag"
fun LazyListScope.homePageBrandTagUi() {
    item(key = KEY_BRAND) {
        Image(painter = painterResource(id = R.drawable.fresh_veggies_tag),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp),
            contentDescription = "Discount")
    }
}