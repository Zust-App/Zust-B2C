package `in`.opening.area.zustapp.address.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

private val itemModifier = Modifier
    .fillMaxWidth()
    .padding(vertical = 2.dp, horizontal = 16.dp)
    .background(color = Color.White, shape = RoundedCornerShape(4.dp))
    .wrapContentHeight()
    .padding(vertical = 12.dp, horizontal = 8.dp)

@Composable
fun SearchPlacesResultItemUi(data: SearchPlacesDataModel, callback: (SearchPlacesDataModel) -> Unit) {
    ConstraintLayout(
        modifier = itemModifier.clickable {
            callback.invoke(data)
        }
    ) {
        val (locationIcon, placesTitleTextView, placesDescriptionTextView) = createRefs()
        Image(
            painter = painterResource(id = R.drawable.ic_outline_location_on_24),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .constrainAs(locationIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
        Text(
            text = data.mainText ?: "",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = ZustTypography.body2,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(placesTitleTextView) {
                    start.linkTo(locationIcon.end, 8.dp)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = data.description ?: "",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = ZustTypography.subtitle1,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            modifier = Modifier
                .constrainAs(placesDescriptionTextView) {
                    start.linkTo(placesTitleTextView.start)
                    end.linkTo(parent.end)
                    top.linkTo(placesTitleTextView.bottom)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
        )

    }
}