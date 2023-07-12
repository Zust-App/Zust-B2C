package zustElectronics.zeLanding.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import coil.request.ImageRequest
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import `in`.opening.area.zustapp.ui.theme.montserratFontFamily
import zustElectronics.zeLanding.models.ZeLandingProductsData
import zustElectronics.zeProductDetails.ZeProductDetailsActivity

@Composable
fun ZeHomeTrendingVerticalItems(singleData: ZeLandingProductsData) {
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 20.dp, vertical = 12.dp)
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = colorResource(id = R.color.white), shape = RoundedCornerShape(8.dp))
        .clickable {
            if (singleData.id != null) {
                val zeProductDetailsIntent = Intent(context, ZeProductDetailsActivity::class.java)
                zeProductDetailsIntent.putExtra(ZeProductDetailsActivity.ZE_PRODUCT_ID, singleData.id)
                context.startActivity(zeProductDetailsIntent)
            }
        }
        .zIndex(1f)) {
        val (thumbnail, title, price, actionIcon, ratingIcon, rating) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://www.voltas.com/cdn/shop/products/Vertis-Prism-adj-ac-22aug_2_1080x.jpg?v=1661145590")
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(thumbnail) {
                    top.linkTo(parent.top, dp_16)
                    start.linkTo(parent.start, dp_16)
                    bottom.linkTo(parent.bottom, dp_16)
                }
                .width(145.dp)
                .height(145.dp)
                .clickable {

                }
        )

        Text(text = singleData.name, modifier = Modifier.constrainAs(title) {
            top.linkTo(parent.top, dp_16)
            end.linkTo(parent.end, dp_16)
            start.linkTo(thumbnail.end, dp_12)
            width = Dimension.fillToConstraints
        }, fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W600)

        Text(text = "Start From ₹20000",
            modifier = Modifier.constrainAs(price) {
                top.linkTo(title.bottom, dp_16)
                start.linkTo(thumbnail.end, dp_12)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }, fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W500)

        Icon(painter = painterResource(id = R.drawable.ze_arrow_icon),
            contentDescription = "",
            modifier = Modifier.constrainAs(actionIcon) {
                bottom.linkTo(parent.bottom, dp_16)
                end.linkTo(parent.end, dp_16)
            })


        Image(painter = painterResource(id = R.drawable.star_icon),
            contentDescription = "",
            modifier = Modifier.constrainAs(ratingIcon) {
                top.linkTo(price.bottom, dp_12)
                start.linkTo(title.start)
            })

        Text(text = "2.5",
            modifier = Modifier.constrainAs(rating) {
                top.linkTo(ratingIcon.top)
                bottom.linkTo(ratingIcon.bottom)
                start.linkTo(ratingIcon.end, dp_8)
            }, fontFamily = montserratFontFamily,
            fontWeight = FontWeight.W500)
    }
}
