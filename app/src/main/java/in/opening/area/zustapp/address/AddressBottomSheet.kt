package `in`.opening.area.zustapp.address

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.databinding.AddressSheetPageBinding
import `in`.opening.area.zustapp.address.AddressSearchActivity.Companion.ADDRESS_TEXT
import `in`.opening.area.zustapp.address.adapter.PlacesAdapter
import `in`.opening.area.zustapp.address.adapter.SavedAddressAdapter
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.utils.AddressUtils
import `in`.opening.area.zustapp.uiModels.SearchPlacesUi
import `in`.opening.area.zustapp.uiModels.UserSavedAddressUi
import `in`.opening.area.zustapp.viewmodels.AddressViewModel
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.location.Address
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddressBottomSheet : BottomSheetDialogFragment() {
    private val viewModel: AddressViewModel by viewModels()
    private var binding: AddressSheetPageBinding? = null

    private val savedAddressAdapter: SavedAddressAdapter by lazy {
        SavedAddressAdapter {
            listener?.didTapOnAddAddress(it)
            dialog?.dismiss()
        }
    }

    private var noSaveAddressTag = false
    private var listener: AddressBtmSheetCallback? = null
    private val dividerDrawable: Drawable? by lazy {
        context?.let { ContextCompat.getDrawable(it, R.drawable.divider_1dp) }
    }

    private val searchPlacesAdapter: PlacesAdapter by lazy {
        PlacesAdapter {
            val address = AddressUtils.getLocationFromAddress(it.description, context)
            processWhenReceiveLatLng(address)
            dialog?.dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = AddressSheetPageBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpClickListeners()
        setUpObservers()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AddressBtmSheetCallback) {
            listener = context
        }
    }

    private fun setUpClickListeners() {
        binding?.cancelSheet?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.searchPlacesEditText?.addTextChangedListener { text ->
            if (text != null && text.isNotEmpty()) {
                binding?.clearEditText?.visibility = View.VISIBLE
            } else {
                binding?.clearEditText?.visibility = View.GONE
                showHideLayoutForSearchAndSaved(canShowSearch = false)
            }
            if (text != null && text.length >= AddressSearchActivity.PLACES_SEARCH_THRESHOLD) {
                showHideProgressBar(true)
                viewModel.getSearchResult(text.toString())
            }
        }
        binding?.clearEditText?.setOnClickListener {
            binding?.searchPlacesEditText?.setText("")
        }
        binding?.useCurrentLocationTag?.setOnClickListener {
            listener?.clickOnUseCurrentLocation()
        }
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
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    private fun setUpViews() {
        binding?.searchLocationRecycler?.apply {
            if (context != null && dividerDrawable != null) {
                val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                itemDecorator.setDrawable(dividerDrawable!!)
                addItemDecoration(itemDecorator)
            }
            layoutManager = LinearLayoutManager(context)
            adapter = searchPlacesAdapter
        }
        binding?.savedLocationRecyclerView?.apply {
            if (context != null && dividerDrawable != null) {
                val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                itemDecorator.setDrawable(dividerDrawable!!)
                addItemDecoration(itemDecorator)
            }
            layoutManager = LinearLayoutManager(context)
            adapter = savedAddressAdapter
        }
        binding?.searchPlacesEditText?.setText("")
    }

    private fun setUpObservers() {
        lifecycleScope.launch {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.searchPlacesUiState.collectLatest {
                        parseSearchPlaces(it)
                    }
                }
            }
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.userAddressListUiState.collectLatest {
                        parseResponseForSavedAddress(it)
                    }
                }
            }
        }
        viewModel.getAllAddress()
    }

    private fun parseResponseForSavedAddress(userSavedAddressUi: UserSavedAddressUi) {
        showHideProgressBar(userSavedAddressUi.isLoading)
        when (userSavedAddressUi) {
            is UserSavedAddressUi.UserAddressResponse -> {
                if (userSavedAddressUi.data == null) {
                    savedAddressAdapter.submitList(arrayListOf())
                    showHideLayoutForSearchAndSaved(canShowSearch = false)
                } else {
                    if (userSavedAddressUi.data.addresses.isEmpty()) {
                        showHideSavedAddress(true)
                        noSaveAddressTag = true
                    } else {
                        showHideSavedAddress(false)
                    }
                    savedAddressAdapter.submitList(userSavedAddressUi.data.addresses)
                    showHideLayoutForSearchAndSaved(canShowSearch = false)
                }
            }
            is UserSavedAddressUi.ErrorState -> {
                showToast(userSavedAddressUi.message)
            }
            is UserSavedAddressUi.InitialUi -> {
                showHideProgressBar(userSavedAddressUi.isLoading)
            }
        }
    }

    private fun parseSearchPlaces(searchPlacesUi: SearchPlacesUi) {
        when (searchPlacesUi) {
            is SearchPlacesUi.InitialUi -> {
                showHideProgressBar(searchPlacesUi.isLoading)
            }
            is SearchPlacesUi.SearchPlaceResult -> {
                showHideLayoutForSearchAndSaved(canShowSearch = true)
                searchPlacesAdapter.submitList(searchPlacesUi.data)
                showHideProgressBar(searchPlacesUi.isLoading)
            }
            is SearchPlacesUi.ErrorUi -> {
                showHideProgressBar(searchPlacesUi.isLoading)
                showToast(searchPlacesUi.errorMessage)
            }
        }
    }

    private fun showToast(message: String?) {
        if (message == null) {
            return
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun processWhenReceiveLatLng(address: Address?) {
        if (address == null) {
            return
        }
        if (context != null) {
            val mapActivity = Intent(context, AddressSearchActivity::class.java)
            mapActivity.putExtra(ADDRESS_TEXT, address)
            startActivity(mapActivity)
        }
    }

    private fun showHideLayoutForSearchAndSaved(canShowSearch: Boolean) {
        if (canShowSearch) {
            binding?.searchLocationTitleTag?.visibility = View.VISIBLE
            binding?.searchLocationRecycler?.visibility = View.VISIBLE
            binding?.saveLocationTitleTag?.visibility = View.GONE
            binding?.savedLocationRecyclerView?.visibility = View.GONE
            showHideSavedAddress(false)
        } else {
            binding?.searchLocationTitleTag?.visibility = View.GONE
            binding?.searchLocationRecycler?.visibility = View.GONE
            binding?.saveLocationTitleTag?.visibility = View.VISIBLE
            binding?.savedLocationRecyclerView?.visibility = View.VISIBLE
            if (noSaveAddressTag) {
                showHideSavedAddress(true)
            } else {
                showHideSavedAddress(false)
            }
        }
    }

    private fun showHideProgressBar(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showHideSavedAddress(canShow: Boolean) {
        if (canShow) {
            binding?.noSavedAddressFound?.visibility = View.VISIBLE
        } else {
            binding?.noSavedAddressFound?.visibility = View.GONE
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): AddressBottomSheet {
            val args = Bundle()
            val fragment = AddressBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }


}

interface AddressBtmSheetCallback {
    fun clickOnUseCurrentLocation(){}
    fun didTapOnSearchAddress(){}
    fun didTapOnAddAddress(savedAddress: AddressItem)
    fun didTapOnAddNewAddress(){}
}