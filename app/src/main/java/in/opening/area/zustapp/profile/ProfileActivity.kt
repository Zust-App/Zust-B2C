package `in`.opening.area.zustapp.profile

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.v2.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_ABOUT_US
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_FAQ
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_HELP_CLICK
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_LOGOUT_CLICK
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_MY_ADDRESS
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_MY_ORDERS
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_OPEN_SOURCE
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_PRIVACY_POLICY
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_RATE_US
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_SHARE_APP
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_SUGGEST_ITEMS
import `in`.opening.area.zustapp.analytics.FirebaseAnalytics.Companion.PROFILE_TC_CLICK
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.helpline.HelplineBtmSheet
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.inappreview.InAppReview
import `in`.opening.area.zustapp.login.LoginActivity
import `in`.opening.area.zustapp.profile.components.ProfileMainContainer
import `in`.opening.area.zustapp.utility.moveToInAppWebPage
import `in`.opening.area.zustapp.utility.navigateToReferAndEarn
import `in`.opening.area.zustapp.utility.proceedToLoginActivity
import `in`.opening.area.zustapp.utility.startMyOrders
import `in`.opening.area.zustapp.viewmodels.ProfileViewModel
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
                        ComposeCustomTopAppBar(modifier = Modifier, titleText = getString(R.string.my_profile), null, null) {
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
                FirebaseAnalytics.logEvents(PROFILE_MY_ORDERS)
                this.startMyOrders()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            ProfileActionCallback.MY_ADDRESS -> {
                FirebaseAnalytics.logEvents(PROFILE_MY_ADDRESS)
                showAddressBtmSheet()
            }
            ProfileActionCallback.SUGGEST_PRODUCT -> {
                FirebaseAnalytics.logEvents(PROFILE_SUGGEST_ITEMS)
                showSuggestProductSheet()
            }
            ProfileActionCallback.RATE_US_PLAY_STORE -> {
                FirebaseAnalytics.logEvents(PROFILE_RATE_US)
                inAppReview.showInAppReviewDialog()
            }
            ProfileActionCallback.SHARE_APP -> {
                FirebaseAnalytics.logEvents(PROFILE_SHARE_APP)
                this.navigateToReferAndEarn(profileViewModel.getReferral())
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            //web activity start from here
            ProfileActionCallback.FAQ -> {
                if (profileViewModel.faqUrl() != null) {
                    FirebaseAnalytics.logEvents(PROFILE_FAQ)
                    this.moveToInAppWebPage(profileViewModel.faqUrl()!!, getString(R.string.faq))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.ABOUT_US -> {
                if (profileViewModel.getAboutUsUrl() != null) {
                    FirebaseAnalytics.logEvents(PROFILE_ABOUT_US)
                    this.moveToInAppWebPage(profileViewModel.getAboutUsUrl()!!, getString(R.string.about_us))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.OPEN_SOURCE -> {
                if (profileViewModel.getOpenSourceUrl() != null) {
                    FirebaseAnalytics.logEvents(PROFILE_OPEN_SOURCE)
                    this.moveToInAppWebPage(profileViewModel.getOpenSourceUrl()!!, getString(R.string.open_source))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.TC -> {
                if (profileViewModel.termAndConditionUrl() != null) {
                    FirebaseAnalytics.logEvents(PROFILE_TC_CLICK)
                    this.moveToInAppWebPage(profileViewModel.termAndConditionUrl()!!, getString(R.string.terms_condition))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.HELPLINE -> {
                FirebaseAnalytics.logEvents(PROFILE_HELP_CLICK)
                openHelplineBtmSheet()
            }
            ProfileActionCallback.PRIVACY_POLICY -> {
                if (profileViewModel.privacyPolicyUrl() != null) {
                    FirebaseAnalytics.logEvents(PROFILE_PRIVACY_POLICY)
                    this.moveToInAppWebPage(profileViewModel.privacyPolicyUrl()!!, getString(R.string.privacy_policy))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }
            ProfileActionCallback.LOGOUT -> {
                FirebaseAnalytics.logEvents(PROFILE_LOGOUT_CLICK)
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
        //startAddNewAddressActivity()
        startAddressSearchActivity()
    }

    private fun startAddNewAddressActivity(){
        val newAddressIntent = Intent(this, AddNewAddressActivity::class.java)
        startActivity(newAddressIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun startAddressSearchActivity(){
        val newAddressIntent = Intent(this, AddressSearchActivity::class.java)
        startActivity(newAddressIntent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        //empty
    }
}
