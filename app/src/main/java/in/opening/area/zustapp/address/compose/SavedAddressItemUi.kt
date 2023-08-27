package `in`.opening.area.zustapp.address.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun SavedAddressItemUi(modifier: Modifier, addressItem: AddressItem, callback: (AddressItem) -> Unit) {
    ConstraintLayout(modifier = modifier
        .clickable {
            callback.invoke(addressItem)
        }
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp)) {
        val (labelIcon, labelText, addressText) = createRefs()
        Icon(painter = painterResource(id = R.drawable.home_icon),
            contentDescription = "home",
            modifier = modifier.constrainAs(labelIcon) {
                start.linkTo(parent.start)
                top.linkTo(parent.top, dp_10)
                end.linkTo(labelText.start)
            })

        Text(text = (addressItem.addressType ?: "Home"),
            modifier = modifier.constrainAs(labelText) {
                start.linkTo(labelIcon.end, dp_12)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(addressText.top)
                width = Dimension.fillToConstraints
            }, style = ZustTypography.bodyMedium,
            color = colorResource(id = R.color.app_black))

        Text(text = addressItem.getDisplayString(),
            modifier = modifier.constrainAs(addressText) {
                top.linkTo(labelText.bottom, dp_4)
                bottom.linkTo(parent.bottom)
                start.linkTo(labelIcon.end, dp_12)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, color = Color(0xCC1F1F1F),
            style = ZustTypography.bodySmall)

    }
}