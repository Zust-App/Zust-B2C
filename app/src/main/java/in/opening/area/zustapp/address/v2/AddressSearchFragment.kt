package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.compose.SearchAddressMainUi
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.model.SearchPlacesDataModel
import `in`.opening.area.zustapp.address.utils.AddressUtils
import `in`.opening.area.zustapp.uiModels.LocationAddressUi
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.update


//@AndroidEntryPoint
//class AddressSearchFragment : Fragment() {
//    private val viewModel: AddressViewModel by activityViewModels()
//
//    private var listener: SearchPlaceAndLocationListeners? = null
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return ComposeView(requireContext()).apply {
//            setContent {
//                SearchAddressMainUi(viewModel,
//                    modifier = Modifier.background(color = colorResource(id = R.color.white)), {
//                    processSelectedSearchAddress(it)
//                }) {
//                    handleCurrentLocationClick(it)
//                }
//            }
//        }
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is SearchPlaceAndLocationListeners) {
//            listener = context
//        }
//    }
//
//    private fun processWhenReceiveLatLng(address: Address?) {
//        if (address == null) {
//            return
//        }
//        if (context != null) {
//            viewModel.searchedAddress.update {
//                LocationAddressUi.Success(false, address)
//            }
//            listener?.didReceivedSearchResult(address)
//        }
//    }
//
//    private fun processSelectedSearchAddress(searchPlacesDataModel: SearchPlacesDataModel) {
//        val address = AddressUtils.getLocationFromAddress(searchPlacesDataModel.description, context)
//        processWhenReceiveLatLng(address)
//    }
//
//    private fun handleCurrentLocationClick(data: Any?) {
//        when (data) {
//            is Address -> {
//                processWhenReceiveLatLng(data)
//            }
//            null -> {
//                listener?.didTapOnCurrentLocation()
//            }
//        }
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(): AddressSearchFragment {
//            val args = Bundle()
//            val fragment = AddressSearchFragment()
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}
//
//interface SearchPlaceAndLocationListeners {
//    fun didTapOnSavedAddress(savedAddress: AddressItem)
//    fun didReceivedSearchResult(address: Address?)
//    fun didTapOnCurrentLocation()
//    fun moveToAddressInputPage()
//    fun showSearchPage()
//}
//
interface AddressBtmSheetCallback {
    fun clickOnUseCurrentLocation() {}
    fun didTapOnSearchAddress() {}
    fun didTapOnAddAddress(savedAddress: AddressItem)
    fun didTapOnAddNewAddress() {}
}