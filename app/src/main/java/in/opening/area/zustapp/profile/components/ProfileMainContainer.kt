package `in`.opening.area.zustapp.profile.components

import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.profile.ProfileActionCallback
import `in`.opening.area.zustapp.profile.models.ProfileData
import `in`.opening.area.zustapp.profile.models.ProfileItemViewType
import `in`.opening.area.zustapp.profile.models.getProfileStaticItems
import `in`.opening.area.zustapp.uiModels.UserProfileUi
import `in`.opening.area.zustapp.utility.AppUtility.Companion.showToast
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ProfileMainContainer(innerPadding: PaddingValues, profileViewModel: ProfileViewModel, callback: ProfileActionCallback) {
    val context = LocalContext.current

    val userProfileResponse by profileViewModel.profileUiState.collectAsState(UserProfileUi.InitialUi(false))
    var showHidePgBar by remember {
        mutableStateOf(false)
    }
    val response = userProfileResponse
    showHidePgBar = response.isLoading

    var userProfileData by remember {
        mutableStateOf<ProfileData?>(null)
    }

    when (response) {
        is UserProfileUi.ProfileSuccess -> {
            userProfileData = response.data
        }
        is UserProfileUi.InitialUi -> {
            showHidePgBar = response.isLoading
        }
        is UserProfileUi.ErrorUi -> {
            if (!response.errorMsg.isNullOrEmpty()) {
                showToast(context, response.errorMsg)
            } else {
                showToast(context, response.errors.getTextMsg())
            }
        }
    }

    ConstraintLayout(modifier = Modifier
        .padding(innerPadding)
        .fillMaxWidth()
        .background(color = Color(0xfffffefe))
        .fillMaxHeight()) {
        val (column, loader) = createRefs()
        LazyColumn(modifier = Modifier
            .constrainAs(column) {

            }
            .fillMaxWidth()
            .background(color = Color.White)
            .fillMaxHeight()) {
            if (userProfileData?.user != null) {
                item {
                    DisplayUserInfo(userProfileData!!.user)
                }
                item {
                    ProfileReferralSection(userProfileData!!.refer)
                }
                items(getProfileStaticItems()) { profileItem ->
                    when (profileItem.type) {
                        ProfileItemViewType.TITLE -> {
                            ProfileSubTitleInfo(profileItem)
                        }
                        ProfileItemViewType.DIVIDER -> {
                            Divider(modifier = Modifier
                                .padding(top = 8.dp, start = 20.dp, end = 16.dp)
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(color = Color(0xfff5f6f9)))
                        }
                        else -> {
                            ProfileEachItemContainer(profileItem) {
                                if (it != null) {
                                    callback.handleClick(it)
                                }
                            }
                        }
                    }
                }
                item {
                    AppVersionInfoHolder()
                }
            }
        }
        if (showHidePgBar) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier
                    .constrainAs(loader) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .width(30.dp)
                    .height(30.dp)
            )
        }
    }
}
