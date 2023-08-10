package dynamic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.compose.CustomTopBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.openCallIntent
import `in`.opening.area.zustapp.utility.openWhatsAppOrderIntent
import `in`.opening.area.zustapp.utility.startNonVegSearchActivity
import `in`.opening.area.zustapp.utility.startSearchActivity
import `in`.opening.area.zustapp.utility.startUserProfileActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import non_veg.home.ui.CustomNonVegHomeTopBar
import non_veg.home.ui.ZustNvEntryMainUi
import non_veg.home.viewmodel.ZustNvEntryViewModel

@AndroidEntryPoint
class NonVegHomeFragment : Fragment(), AddressBtmSheetCallback {
    private val zustNvEntryViewModel: ZustNvEntryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                Scaffold(modifier = Modifier, content = { profilePaddingValues ->
                    ZustNvEntryMainUi(paddingValues = profilePaddingValues) {
                        handleActionIntent(ACTION.SEARCH_PRODUCT)
                    }
                }, topBar = {
                    CustomNonVegHomeTopBar(modifier = Modifier) {
                        handleActionIntent(it)
                    }
                })
                LaunchedEffect(key1 = Unit, block = {
                    zustNvEntryViewModel.getUserSavedAddress()
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onStart() {
        super.onStart()
    }

    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        zustNvEntryViewModel.saveLatestAddress(address)
        zustNvEntryViewModel.getUserSavedAddress()
    }

    override fun didTapOnAddNewAddress() {
        openAddressSearchActivity()
    }

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                zustNvEntryViewModel.getUserSavedAddress()
            }
        }
    }

    private fun openAddressSearchActivity() {
        context?.let {
            val newAddressIntent = Intent(it, AddressSearchActivity::class.java)
            startAddNewAddressActivity.launch(newAddressIntent)
        }
    }

    private fun handleActionIntent(action: ACTION) {
        when (action) {
            ACTION.OPEN_LOCATION -> {
                val bottomSheetV2 = AddressBottomSheetV2.newInstance()
                childFragmentManager.showBottomSheetIsNotPresent(bottomSheetV2, AddressBottomSheetV2.SHEET_TAG)
            }

            ACTION.OPEN_USER_BOOKING -> {
                context?.startUserProfileActivity()
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            ACTION.SEARCH_PRODUCT -> {
                context?.startNonVegSearchActivity()
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }


            ACTION.OPEN_PROFILE -> {
                context?.startUserProfileActivity()
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            ACTION.ORDER_WA -> {
                context?.openWhatsAppOrderIntent()
            }

            ACTION.PHONE_CALL -> {
                context?.openCallIntent("74564062907")
            }

            else -> {}
        }
    }


    companion object {
        fun newInstance() =
            NonVegHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}