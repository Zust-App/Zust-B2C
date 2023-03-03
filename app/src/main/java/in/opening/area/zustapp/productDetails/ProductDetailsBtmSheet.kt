package `in`.opening.area.zustapp.productDetails

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.coupon.model.getTextMsg
import `in`.opening.area.zustapp.product.model.CreateCartReqModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.uiModels.CreateCartResponseUi
import `in`.opening.area.zustapp.uiModels.VALUE
import `in`.opening.area.zustapp.uiModels.productList.SingleProductUi
import `in`.opening.area.zustapp.utility.AppUtility
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.viewmodels.ACTION
import `in`.opening.area.zustapp.viewmodels.ProductListingViewModel
import `in`.opening.area.zustapp.databinding.ProductDetailsBottomSheetBinding
import android.app.Dialog
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailsBtmSheet : BottomSheetDialogFragment() {
    var binding: ProductDetailsBottomSheetBinding? = null
    private var productSingleItem: ProductSingleItem? = null

    private val viewModel: ProductListingViewModel by activityViewModels()

    private var listener: ProductDetailsCallback? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ProductDetailsCallback) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            productSingleItem = it.getParcelable(PRODUCT_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ProductDetailsBottomSheetBinding.inflate(layoutInflater)
        if (productSingleItem == null) {
            dialog?.dismiss()
        }
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
        setUpData(productSingleItem)
        setUpObserver()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.background =
                context?.let { it1 -> ContextCompat.getDrawable(it1, android.R.color.transparent) }
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
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

    companion object {
        private const val PRODUCT_KEY = "product_key"
        const val TAG = "product_detail_sheet"
        fun newInstance(productSingleItem: ProductSingleItem?): ProductDetailsBtmSheet {
            val args = Bundle()
            args.putParcelable(PRODUCT_KEY, productSingleItem)
            val fragment = ProductDetailsBtmSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private fun setUpData(productSingleItem: ProductSingleItem?) {
        if (productSingleItem == null) {
            return
        }
        binding?.currPrice?.text = buildString {
            append("₹")
            append(productSingleItem.price)
        }
        binding?.mrpPrice?.text = buildString {
            append("₹")
            append(productSingleItem.mrp)
        }
        if (binding?.mrpPrice?.paintFlags != null) {
            binding!!.mrpPrice.paintFlags = binding!!.mrpPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding?.productName?.text = productSingleItem.productName
        if (productSingleItem.discountPercentage > 0.0) {
            binding?.discountTag?.text = buildString {
                append(productSingleItem.discountPercentage)
                append("% OFF")
            }
        }
        binding?.itemQuantityTextView?.text = buildString {
            append(ProductUtils.getNumberDisplayValue(productSingleItem.quantity))
            append(" ")
            append(productSingleItem.quantityUnit)
        }

        if (!productSingleItem.thumbnail.isNullOrEmpty()) {
            if (context != null && binding?.productDetailImageIcon != null) {
                Glide.with(requireContext()).load(productSingleItem.thumbnail).into(binding?.productDetailImageIcon!!)
            }
        }
        if (productSingleItem.description.isNotEmpty()) {
            binding?.productDescriptionText?.text = productSingleItem.description
        }

    }

    private fun updateProductCountByUser(productSingleItem: ProductSingleItem?) {
        if (productSingleItem == null) {
            return
        }
        if (productSingleItem.itemCountByUser <= 0) {
            binding?.addRemoveContainer?.visibility = View.GONE
            binding?.itemCountTextView?.text = ""
            binding?.addItemContainer?.visibility = View.VISIBLE
        } else {
            binding?.addItemContainer?.visibility = View.GONE
            binding?.addRemoveContainer?.visibility = View.VISIBLE
            binding?.itemCountTextView?.text = productSingleItem.itemCountByUser.toString()
        }
    }

    private fun setUpClickListeners() {
        binding?.cancelSheet?.setOnClickListener {
            dialog?.dismiss()
        }
        binding?.increaseItemButton?.setOnClickListener {
            if (productSingleItem != null) {
                viewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
            }
        }
        binding?.decreaseItemButton?.setOnClickListener {
            if (productSingleItem != null) {
                viewModel.updateProductCount(productSingleItem, ACTION.DECREASE)
            }
        }
        binding?.addItemContainer?.setOnClickListener {
            if (productSingleItem != null) {
                viewModel.updateProductCount(productSingleItem, ACTION.INCREASE)
            }
        }
        binding?.bottomBarContainer?.root?.setOnClickListener {
            if (!viewModel.isCreateCartOnGoing()) {
                viewModel.createCartOrderWithServer(VALUE.S)
            } else {
                AppUtility.showToast(context, "Please wait")
            }
        }
    }


    private fun setUpObserver() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.singleProductUiState.collect {
                    updateProductCountByUser(it.productSingleItem)
                }
            }
            launch {
                viewModel.addToCartFlow.collect {
                    updateBottomBar(it)
                }
            }
            launch {
                viewModel.createCartUiState.collect {
                    parseAddToCartResponse(it)
                }
            }
        }
    }

    private fun updateBottomBar(it: CreateCartReqModel) {
        if (it.totalItemCount <= 0) {
            binding?.bottomBarContainer?.root?.visibility = View.GONE
            binding?.bottomBarContainer?.totalPriceText?.text = ""
            binding?.bottomBarContainer?.itemCountTextView?.text = ""
        } else {
            binding?.bottomBarContainer?.root?.visibility = View.VISIBLE
            binding?.bottomBarContainer?.totalPriceText?.text = buildString {
                append("₹")
                append(it.calculatedPrice)
            }
            binding?.bottomBarContainer?.itemCountTextView?.text = buildString {
                append(it.totalItemCount)
                append(" Items")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.singleProductUiState.update {
            SingleProductUi(null, null)
        }
    }

    private fun parseAddToCartResponse(cartUiState: CreateCartResponseUi) {
        showHidePgBar(canShow = cartUiState.isLoading)
        when (cartUiState) {
            is CreateCartResponseUi.ErrorUi -> {
                if (!cartUiState.errorMsg.isNullOrEmpty()) {
                    AppUtility.showToast(context, cartUiState.errorMsg)
                } else {
                    AppUtility.showToast(context, cartUiState.errors?.getTextMsg())
                }
            }
            is CreateCartResponseUi.CartSuccess -> {
                listener?.startOrderSummary(cartUiState.data)
            }
            is CreateCartResponseUi.InitialUi -> {

            }
        }
    }

    private fun showHidePgBar(canShow: Boolean) {
        if (canShow) {
            binding?.progressBar?.visibility = View.VISIBLE
        } else {
            binding?.progressBar?.visibility = View.GONE
        }
    }
}