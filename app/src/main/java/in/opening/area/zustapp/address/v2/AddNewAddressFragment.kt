package `in`.opening.area.zustapp.address.v2

import `in`.opening.area.zustapp.address.AddressFragmentCommunicator
import `in`.opening.area.zustapp.address.model.SaveAddressPostModel
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.databinding.FragmentAddNewAddressBinding
import `in`.opening.area.zustapp.uiModels.SaveUserAddressUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNewAddressFragment : Fragment() {

    private var binding: FragmentAddNewAddressBinding? = null

    private val viewModel: AddressViewModel by viewModels()
    private var listener: AddressFragmentCommunicator? = null

    private var savedAddressPostMode: SaveAddressPostModel = SaveAddressPostModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddressFragmentCommunicator) {
            listener = context
        }
        if (parentFragment != null) {
            listener = parentFragment as AddressFragmentCommunicator
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddNewAddressBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        setUpClickListeners()
    }


    private fun setUpObservers() {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {

                }
            }
            launch {
                viewModel.saveUserAddressUiState.collect {
                    parseSaveAddress(it)
                }
            }
        }
        viewModel.getAllAddress()
    }

    private fun setUpClickListeners() {
        binding?.saveAddressButton?.setOnClickListener {
            val enteredPinCode = binding?.pinCodeEditText?.text?.toString()
            val houseNumberAndFloor = binding?.houseNumEditText?.text?.toString()
            val landmark = binding?.areaColony?.text?.toString()
            if (enteredPinCode.isNullOrEmpty()) {
                AppUtility.showToast(context, "Please enter Pincode")
                return@setOnClickListener
            }
            if (enteredPinCode.length != 6) {
                AppUtility.showToast(context, "Please enter correct PinCode")
                return@setOnClickListener
            }
            if (houseNumberAndFloor.isNullOrEmpty()) {
                AppUtility.showToast(context, "Please enter House number")
                return@setOnClickListener
            }
            if (landmark.isNullOrEmpty()) {
                AppUtility.showToast(context, "Please enter Area name")
                return@setOnClickListener
            }
            savedAddressPostMode.pinCode = enteredPinCode
            savedAddressPostMode.houseNumberAndFloor = houseNumberAndFloor
            savedAddressPostMode.landmark = landmark
            viewModel.checkIsDeliverablePinCodeOrNot(enteredPinCode)
        }
        binding?.tagSeeAlreadySavedAddress?.setOnClickListener {
            listener?.showAlreadyAddedFragment()
        }
    }


    private fun parseSaveAddress(response: SaveUserAddressUi) {
        showHideProgress(response.isLoading)
        when (response) {
            is SaveUserAddressUi.SaveAddressUi -> {
                if (response.data?.id == -1 || response.data?.id == null) {
                    AppUtility.showToast(context, "Something went wrong")
                } else {
                    AppUtility.showToast(context, "Address Saved successfully")

                }
            }
            is SaveUserAddressUi.ErrorUi -> {
                AppUtility.showToast(context, response.errors.getTextMsg())
            }
            else -> {}
        }
    }

    private fun showHideProgress(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): AddNewAddressFragment {
            val args = Bundle()
            val fragment = AddNewAddressFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
