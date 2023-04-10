package `in`.opening.area.zustapp.profile

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddressAddSelectActivity
import `in`.opening.area.zustapp.address.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.helpline.HelplineBtmSheet
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.inappreview.InAppReview
import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.orderHistory.MyOrdersActivity
import `in`.opening.area.zustapp.profile.components.ProfileMainContainer
import `in`.opening.area.zustapp.utility.moveToInAppWebPage
import `in`.opening.area.zustapp.utility.navigateToReferAndEarn
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.utility.startMyOrders
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
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), ProfileActionCallback, AddressBtmSheetCallback {
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
        setUpObserver()
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            profileViewModel.moveToLoginPage.collectLatest {
                if (it) {
                    profileViewModel.removeUserLocalData()
                    this@ProfileActivity.proceedToLoginActivity()
                    finish()
                }
            }
        }
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
                this.startMyOrders()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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
                this.navigateToReferAndEarn(profileViewModel.getReferral())
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            //web activity start from here
            ProfileActionCallback.FAQ -> {
                if (profileViewModel.faqUrl() != null) {
                    this.moveToInAppWebPage(profileViewModel.faqUrl()!!, "Faq")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.ABOUT_US -> {
                if (profileViewModel.getAboutUsUrl() != null) {
                    this.moveToInAppWebPage(profileViewModel.getAboutUsUrl()!!, "About us")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.OPEN_SOURCE -> {
                if (profileViewModel.getOpenSourceUrl() != null) {
                    this.moveToInAppWebPage(profileViewModel.getOpenSourceUrl()!!, "Open source")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.TC -> {
                if (profileViewModel.termAndConditionUrl() != null) {
                    this.moveToInAppWebPage(profileViewModel.termAndConditionUrl()!!, "Terms & Condition")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.HELPLINE -> {
                openHelplineBtmSheet()
            }
            ProfileActionCallback.PRIVACY_POLICY -> {
                if (profileViewModel.privacyPolicyUrl() != null) {
                    this.moveToInAppWebPage(profileViewModel.privacyPolicyUrl()!!, "Privacy Policy")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
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


    private fun openHelplineBtmSheet() {
        val helpAndSupportBtmSheet: HelplineBtmSheet = HelplineBtmSheet.newInstance()
        helpAndSupportBtmSheet.show(supportFragmentManager, "help_support")
    }


    override fun didTapOnAddNewAddress() {
        val newAddressIntent = Intent(this, AddressAddSelectActivity::class.java)
        startActivity(newAddressIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        //empty
    }
}
