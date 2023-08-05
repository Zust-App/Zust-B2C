package non_veg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.extensions.collectAsStateLifecycleAware
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.ui.theme.ZustTypography
import `in`.opening.area.zustapp.ui.theme.dp_12
import `in`.opening.area.zustapp.ui.theme.dp_4
import `in`.opening.area.zustapp.ui.theme.dp_6
import `in`.opening.area.zustapp.ui.theme.dp_8
import non_veg.compose_nv.modifier
import non_veg.home.ui.ZustNvEntryMainUi
import non_veg.home.viewmodel.ZustNvEntryViewModel

@AndroidEntryPoint
class ZustNonVegEntryActivity : AppCompatActivity() {
    private val zustNvEntryViewModel: ZustNvEntryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = modifier, content = { profilePaddingValues ->
                ZustNvEntryMainUi( paddingValues = profilePaddingValues)
            }, topBar = {
                CustomNonVegHomeTopBar(Modifier, viewModel = zustNvEntryViewModel) {

                }
            })
        }
        zustNvEntryViewModel.getNonVegMerchantDetails("110011")
    }
}

@Composable
private fun CustomNonVegHomeTopBar(
    modifier: Modifier,
    viewModel: ZustNvEntryViewModel,
    callback: (ACTION) -> Unit,
) {
    val userAddress by viewModel.userLocationFlow.collectAsState(initial = UserLocationDetails())

    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .fillMaxWidth()
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
        .background(color = colorResource(id = R.color.white))
        .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        val (
            locationTag, locationSubTitle,
            locationIcon, changeLanguageIcon, profileIcon, changeLocationIcon,
        ) = createRefs()

        Icon(painter = painterResource(id = R.drawable.simple_location_icon),
            contentDescription = "location", tint = colorResource(id = R.color.white),
            modifier = Modifier
                .height(17.dp)
                .width(12.dp)
                .constrainAs(locationIcon) {
                    top.linkTo(locationTag.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(locationTag.bottom)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                })

        Text(text = "Patna", color = colorResource(id = R.color.white),
            modifier = modifier
                .constrainAs(locationTag) {
                    top.linkTo(parent.top)
                    start.linkTo(locationIcon.end, dp_8)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                }, style = ZustTypography.body1)

        Icon(painter = painterResource(id = R.drawable.down_arrow),
            contentDescription = "location", tint = colorResource(id = R.color.white),
            modifier = Modifier
                .height(10.dp)
                .width(12.dp)
                .constrainAs(changeLocationIcon) {
                    top.linkTo(locationTag.top, dp_4)
                    start.linkTo(locationTag.end, dp_6)
                    bottom.linkTo(locationTag.bottom)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_LOCATION)
                })
        Text(
            text = userAddress.fullAddress ?: "Delivery in Patna",
            style = ZustTypography.subtitle1,
            color = colorResource(id = R.color.white),
            modifier = modifier.constrainAs(locationSubTitle) {
                top.linkTo(locationTag.bottom, dp_4)
                start.linkTo(parent.start)
                end.linkTo(profileIcon.start, dp_8)
                width = Dimension.fillToConstraints
            },
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )

        Icon(painter = painterResource(id = R.drawable.new_profile_icon),
            tint = colorResource(id = R.color.white),
            contentDescription = "profile", modifier = modifier
                .constrainAs(profileIcon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .clickable {
                    callback.invoke(ACTION.OPEN_PROFILE)
                }
                .clip(shape = RoundedCornerShape(8.dp)))
    }
}