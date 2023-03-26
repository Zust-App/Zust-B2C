package `in`.opening.area.zustapp.profile

import `in`.opening.area.zustapp.BuildConfig
import `in`.opening.area.zustapp.address.AddressBottomSheetV2
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.helpline.HelplineBtmSheet
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.inappreview.InAppReview
import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.orderHistory.MyOrdersActivity
import `in`.opening.area.zustapp.profile.components.ProfileMainContainer
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
import `in`.opening.area.zustapp.webpage.InAppWebActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), ProfileActionCallback {
    private val profileViewModel: ProfileViewModel by viewModels()

    private val inAppReview: InAppReview by lazy { InAppReview(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        ComposeCustomTopAppBar(modifier = Modifier, titleText = "My Profile", null, null) {
                            handleAction(it)
                        }
                    },
                    content = { innerPadding ->
                        ProfileMainContainer(innerPadding, profileViewModel, this)
                    }
                )
            }
        }
        profileViewModel.getUserProfileResponse()
    }

    override fun handleClick(code: Int, data: String?) {
        handleClickRedirection(code)
    }

    private fun handleAction(action: ACTION) {
        if (action == ACTION.NAV_BACK) {
            finish()
        }
    }

    private fun handleClickRedirection(eventCode: Int) {
        when (eventCode) {
            ProfileActionCallback.MY_ORDER -> {
                val orderHistoryIntent = Intent(this, MyOrdersActivity::class.java)
                startActivity(orderHistoryIntent)
            }
            ProfileActionCallback.MY_ADDRESS -> {
                showAddressBtmSheet()
            }
            ProfileActionCallback.SUGGEST_PRODUCT -> {
                showSuggestProductSheet()
            }
            ProfileActionCallback.RATE_US_PLAY_STORE -> {
                inAppReview.showInAppReviewDialog()
            }
            ProfileActionCallback.SHARE_APP -> {
                if (profileViewModel.playStoreUrl() != null) {
                    val shareText = "Hey check out my app at: ${profileViewModel.playStoreUrl()}"
                    AppUtility.showShareIntent(this, shareText)
                }
            }
            //web activity start from here
            ProfileActionCallback.FAQ -> {
                if (profileViewModel.faqUrl() != null) {
                    startInAppWebActivity(profileViewModel.faqUrl())
                }
            }
            ProfileActionCallback.ABOUT_US -> {
                if (profileViewModel.getAboutUsUrl() != null) {
                    startInAppWebActivity(profileViewModel.getAboutUsUrl())
                }
            }
            ProfileActionCallback.OPEN_SOURCE -> {
                if (profileViewModel.getOpenSourceUrl() != null) {
                    startInAppWebActivity(profileViewModel.getOpenSourceUrl())
                }
            }
            ProfileActionCallback.TC -> {
                if (profileViewModel.termAndConditionUrl() != null) {
                    startInAppWebActivity(profileViewModel.termAndConditionUrl())
                }
            }
            ProfileActionCallback.HELPLINE -> {
                openHelplineBtmSheet()
            }
            ProfileActionCallback.PRIVACY_POLICY -> {
                if (profileViewModel.privacyPolicyUrl() != null) {
                    startInAppWebActivity(profileViewModel.privacyPolicyUrl())
                }
            }
            ProfileActionCallback.LOGOUT -> {
                profileViewModel.logoutUser()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            //web activity end here
        }
    }

    private fun showAddressBtmSheet() {
        val addressBottomSheet: AddressBottomSheetV2 = AddressBottomSheetV2.newInstance()
        supportFragmentManager.showBottomSheetIsNotPresent(addressBottomSheet, AddressBottomSheetV2.SHEET_TAG)
    }

    private fun showSuggestProductSheet() {
        val suggestProductSheet: SuggestProductBtmSheet = SuggestProductBtmSheet.newInstance()
        suggestProductSheet.show(supportFragmentManager, "suggest_product")
    }

    private fun startInAppWebActivity(url: String? = null) {
        if (url == null) {
            return
        }
        val inAppWebActivity = Intent(this, InAppWebActivity::class.java)
        inAppWebActivity.putExtra(InAppWebActivity.WEB_URL, url)
        inAppWebActivity.putExtra(InAppWebActivity.TITLE_TEXT, "Terms & Conditions")
        startActivity(inAppWebActivity)
    }

    private fun openHelplineBtmSheet() {
        val helpAndSupportBtmSheet: HelplineBtmSheet = HelplineBtmSheet.newInstance()
        helpAndSupportBtmSheet.show(supportFragmentManager, "help_support")
    }
}
