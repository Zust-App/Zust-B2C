package `in`.opening.area.zustapp.home.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.ui.theme.Typography_Montserrat
import `in`.opening.area.zustapp.ui.theme.dp_16
import `in`.opening.area.zustapp.ui.theme.dp_8
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope

@Composable
fun HomePageErrorUi(searchField: ConstrainedLayoutReference,
                    notDeliverHere: ConstrainedLayoutReference,
                    constraintLayoutScope: ConstraintLayoutScope) {
    constraintLayoutScope.apply {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .constrainAs(notDeliverHere) {
                top.linkTo(searchField.bottom, dp_16)
                bottom.linkTo(parent.bottom, dp_8)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Something went wrong please try again",
                color = colorResource(id = R.color.app_black),
                style = Typography_Montserrat.body1)

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Retry",
                color = colorResource(id = R.color.app_black),
                style = Typography_Montserrat.body2)
        }
    }
}