package `in`.opening.area.zustapp.compose

import androidx.compose.runtime.Composable
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_20
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope

@Composable
fun ErrorRetryCaseUiForProductList(layoutScope: ConstraintLayoutScope, otherCategoryUi: ConstrainedLayoutReference) {

    layoutScope.apply {
        val (noProductFoundContainer) = createRefs()
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .constrainAs(noProductFoundContainer) {
                top.linkTo(otherCategoryUi.bottom, dp_8)
                bottom.linkTo(parent.bottom, 8.dp)
                start.linkTo(parent.start, dp_20)
                end.linkTo(parent.end, dp_20)
            }) {
            val (container) = createRefs()
            Column(modifier = Modifier
                .constrainAs(container) {
                    top.linkTo(parent.top, dp_8)
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start, dp_20)
                    end.linkTo(parent.end, dp_20)
                }
                .wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "No product found", style = Typography_Montserrat.body1,
                    color = colorResource(id = R.color.app_black))
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Don't worry it will available soon",
                    style = Typography_Montserrat.body1,
                    fontSize = 20.sp, color = colorResource(id = R.color.app_black))
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white)),
                    modifier = Modifier
                        .background(color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(12.dp))
                        .clip(shape = RoundedCornerShape(12.dp)),
                ) {
                    Text(text = "Suggest Products", style = Typography_Montserrat.body1,
                        color = colorResource(id = R.color.new_material_primary))
                }
            }

        }
    }
}