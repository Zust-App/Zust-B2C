package `in`.opening.area.zustapp.locationV2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.BaseActivityWithLocation
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.GoogleMapsAddressActivity
import `in`.opening.area.zustapp.databinding.ApartmentListingBtmSheetBinding
import `in`.opening.area.zustapp.locationV2.models.ApartmentData
import `in`.opening.area.zustapp.locationV2.models.convertToAddressModel
import `in`.opening.area.zustapp.locationV2.viewModel.ApartmentListingViewModel

@AndroidEntryPoint
class ApartmentListingBtmSheet : BottomSheetDialogFragment() {
    private var binding: ApartmentListingBtmSheetBinding? = null

    private val apartmentListingViewModel: ApartmentListingViewModel? by activityViewModels()

    private var callback: ApartmentListingBtmSheetCallback? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ApartmentListingBtmSheetCallback) {
            callback = context;
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (binding == null) {
            binding = ApartmentListingBtmSheetBinding.inflate(layoutInflater)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpComposeView()
    }

    private fun setUpComposeView() {
        binding?.composeView?.setContent {
            apartmentListingViewModel?.let {
                ApartmentListingContainerUi(apartmentListingViewModel!!) {
                    moveToAddressInputPage(it)
                    dialog?.dismiss()
                }
            }
        }
        apartmentListingViewModel?.getApartmentListing()
    }

    private fun moveToAddressInputPage(apartmentData: ApartmentData) {
        callback?.didTapOnListedApartment(apartmentData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        fun newInstance(): ApartmentListingBtmSheet {
            val args = Bundle()
            val fragment = ApartmentListingBtmSheet()
            fragment.arguments = args
            return fragment
        }
    }

    interface ApartmentListingBtmSheetCallback {
        fun didTapOnListedApartment(apartmentData: ApartmentData)
    }
}