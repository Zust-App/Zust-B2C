package dynamic

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.home.ACTION
import `in`.opening.area.zustapp.utility.startNonVegSearchActivity
import non_veg.common.CustomNonVegBottomBarView
import non_veg.home.ui.CustomNonVegHomeTopBar
import non_veg.home.ui.ZustNvEntryMainUi
import non_veg.home.viewmodel.ZustNvEntryViewModel
import zustbase.utility.handleActionIntent
import zustbase.utility.moveToCartDetailsActivity

@AndroidEntryPoint
class NonVegHomeFragment : Fragment(), AddressBtmSheetCallback {
    private val zustNvEntryViewModel: ZustNvEntryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner)
            )
            setContent {
                Scaffold(modifier = Modifier, content = { profilePaddingValues ->
                    ZustNvEntryMainUi(paddingValues = profilePaddingValues, changeLocation = {
                        (activity as? AppCompatActivity?)?.handleActionIntent(ACTION.OPEN_LOCATION, fragmentManager = childFragmentManager)
                    }) {
                        (activity as? AppCompatActivity?)?.handleActionIntent(ACTION.SEARCH_NON_VEG, fragmentManager = childFragmentManager)
                    }
                }, topBar = {
                    CustomNonVegHomeTopBar(modifier = Modifier) {
                        (activity as? AppCompatActivity?)?.handleActionIntent(it, fragmentManager = childFragmentManager)
                    }
                }, bottomBar = {
                    CustomNonVegBottomBarView(viewModel = zustNvEntryViewModel, proceedToCartClick = {
                        zustNvEntryViewModel.createNonVegCart()
                    }, cartDataCallback = {
                        context?.moveToCartDetailsActivity(it)
                    })
                })
                LaunchedEffect(key1 = Unit, block = {
                    zustNvEntryViewModel.getUserSavedAddress()
                    zustNvEntryViewModel.getUserLatestLocalCartDetails()
                })
            }
        }
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


    companion object {
        fun newInstance() =
            NonVegHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}