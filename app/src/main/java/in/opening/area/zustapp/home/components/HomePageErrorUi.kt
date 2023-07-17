package `in`.opening.area.zustapp.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import `in`.opening.area.zustapp.network.ApiRequestManager

@Composable
fun HomePageErrorUi(
    errorCode: Int?,
    searchField: ConstrainedLayoutReference,
    notDeliverHere: ConstrainedLayoutReference,
    constraintLayoutScope: ConstraintLayoutScope,
    retryCallback: () -> Unit,
    changeLocation: () -> Unit,
) {
    constraintLayoutScope.apply {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .constrainAs(notDeliverHere) {
                top.linkTo(searchField.bottom)
                bottom.linkTo(parent.bottom, dp_8)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(60.dp))
            if (errorCode == ApiRequestManager.NOT_COVERAGE_ERROR_CODE) {
                Image(painter = painterResource(id = R.drawable.not_serviceable_area), contentDescription = "", modifier =
                Modifier
                    .height(150.dp)
                    .width(150.dp))
                Text(text = "Zust is not available in your area.\n" +
                        "Please select a different location.",
                    color = colorResource(id = R.color.app_black),
                    style = ZustTypography.body2,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                    textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    changeLocation.invoke()
                }, modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .background(color = colorResource(id = R.color.new_material_primary),
                        shape = RoundedCornerShape(size = 12.dp)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.new_material_primary))) {
                    Text(text = "Change Location",
                        style = ZustTypography.body1,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = colorResource(id = R.color.white))
                }
            } else {
                Text(text = "Something went wrong please try again",
                    color = colorResource(id = R.color.red_primary),
                    style = ZustTypography.body2)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(onClick = {
                    retryCallback.invoke()
                }) {
                    Text(text = "Retry Again",
                        color = colorResource(id = R.color.app_black),
                        style = ZustTypography.body1)
                }
            }
        }
    }
}