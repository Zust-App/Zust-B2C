package `in`.opening.area.zustapp.onboarding.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val listOfOnBoardingText = arrayListOf("Fresh food, right at your \nfingertips", "The fastest way to a full \npantry.", "Never miss a grocery item \nagain with Zust.")

@Composable
fun OnBoardingSingleItemUi(index: Int) {
    Column {
        Text(text = listOfOnBoardingText[index],
            color = colorResource(id = R.color.new_material_primary),
            fontSize = 18.sp,
            style = ZustTypography.bodyMedium,
            textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))
    }
}