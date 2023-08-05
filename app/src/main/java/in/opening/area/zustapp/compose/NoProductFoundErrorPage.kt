package `in`.opening.area.zustapp.compose

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension

@Composable
fun NoProductFoundErrorPage(
    layoutScope: ConstraintLayoutScope,
    topReference: ConstrainedLayoutReference,
    suggestCallback: () -> Unit,
) {

    layoutScope.apply {
        val (noProductFoundContainer) = createRefs()
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(noProductFoundContainer) {
                top.linkTo(topReference.bottom, dp_24)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            val (container) = createRefs()
            Column(modifier = Modifier
                .constrainAs(container) {
                    top.linkTo(parent.top, dp_8)
                    start.linkTo(parent.start, dp_20)
                    end.linkTo(parent.end, dp_20)
                    width = Dimension.fillToConstraints
                }
                .wrapContentHeight()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Oops, Didn't find what you\n" +
                        "are looking for?",
                    style = ZustTypography.body1,
                    fontSize = 18.sp, color = Color(0xBF1E1E1E))
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { suggestCallback.invoke() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.white),
                        contentColor = colorResource(id = R.color.white)),
                    modifier = Modifier
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(12.dp)),
                ) {
                    Text(text = "Suggest items", style = ZustTypography.body1,
                        color = colorResource(id = R.color.new_material_primary))
                }
            }

        }
    }
}