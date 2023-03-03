package `in`.opening.area.zustapp.profile.components

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.profile.models.ProfileStaticItem
import `in`.opening.area.zustapp.ui.theme.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun ProfileSubTitleInfo(profileStaticItem: ProfileStaticItem) {
    ConstraintLayout(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
        val (title) = createRefs()
        Text(text = profileStaticItem.text,
            style = Typography_Montserrat.body1,
            color = colorResource(id = R.color.app_black), modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })

    }
}