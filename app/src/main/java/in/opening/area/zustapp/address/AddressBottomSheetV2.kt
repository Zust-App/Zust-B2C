package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AlreadySavedAddressFragment
import `in`.opening.area.zustapp.databinding.AddressBtmSheetV2Binding
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddressBottomSheetV2 : BottomSheetDialogFragment(), AddressFragmentCommunicator {
    private var binding: AddressBtmSheetV2Binding? = null
    private var listener: AddressBtmSheetCallback? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddressBtmSheetV2Binding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddressBtmSheetCallback) {
            listener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        showAlreadyAddedFragment()
    }


    private fun setUpClickListeners() {
        binding?.cancelSheet?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.labelAddNewAddress2?.setOnClickListener {
            showAddNewFragment()
        }

    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding = null
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.background = context?.let { it1 -> ContextCompat.getDrawable(it1, android.R.color.transparent) }
            parentLayout?.let { pLayout ->
                val behaviour = BottomSheetBehavior.from(pLayout)
                setupFullHeight(pLayout)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                behaviour.isDraggable = false
            }
        }
        return dialog
    }

    override fun getTheme(): Int {
        return R.style.SheetDialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = (WindowManager.LayoutParams.MATCH_PARENT)
        bottomSheet.layoutParams = layoutParams
    }


    companion object {
        const val SHEET_TAG = "address_v2"
        fun newInstance(): AddressBottomSheetV2 {
            val args = Bundle()
            val fragment = AddressBottomSheetV2()
            fragment.arguments = args
            return fragment
        }
    }

    override fun showAddNewFragment() {
        listener?.didTapOnAddNewAddress()
        dialog?.dismiss()
    }

    override fun showAlreadyAddedFragment() {
        loadAlreadySavedFragment()
    }

    private fun loadAlreadySavedFragment() {
        val alreadySavedAddressFragment = AlreadySavedAddressFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.frameLayout, alreadySavedAddressFragment).commit()
    }

    override fun didTapOnSavedAddress(savedAddress: AddressItem) {
        listener?.didTapOnAddAddress(savedAddress)
        dialog?.dismiss()
    }

    override fun didTapOnCurrentLocation() {

    }

}

interface AddressFragmentCommunicator {
    fun showAddNewFragment()
    fun showAlreadyAddedFragment()
    fun didTapOnSavedAddress(savedAddress: AddressItem)
    fun didTapOnCurrentLocation()

}