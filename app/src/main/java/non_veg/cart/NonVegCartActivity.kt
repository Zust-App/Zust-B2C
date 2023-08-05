package non_veg.cart

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.opening.area.zustapp.address.AddNewAddressActivity
import `in`.opening.area.zustapp.address.AddressSearchActivity
import `in`.opening.area.zustapp.address.model.AddressItem
import `in`.opening.area.zustapp.address.v2.AddressBottomSheetV2
import `in`.opening.area.zustapp.address.v2.AddressBtmSheetCallback
import `in`.opening.area.zustapp.compose.ComposeCustomTopAppBar
import `in`.opening.area.zustapp.extensions.showBottomSheetIsNotPresent
import `in`.opening.area.zustapp.orderSummary.compose.OrderSummaryAction
import `in`.opening.area.zustapp.utility.AppUtility
import non_veg.cart.models.NonVegCartData
import non_veg.cart.models.convertToBasicNonVegCartInfo
import non_veg.cart.ui.NonVegCartMainContainerUi
import non_veg.cart.uiModel.NonVegCartBottomBarUi
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.payment.NonVegPaymentActivity

@AndroidEntryPoint
class NonVegCartActivity : AppCompatActivity(), AddressBtmSheetCallback {
    private val viewModel: NonVegCartViewModel by viewModels()

    companion object {
        const val NON_VEG_CART_ID = "non_veg_cart_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromIntent()
        setContent {
            Scaffold(content = { paddingValues ->
                NonVegCartMainContainerUi(paddingValues, viewModel)
            }, bottomBar = {
                NonVegCartBottomBarUi(viewModel = viewModel, updateOrderCallback = {
                    handleActionOfBottomAppBar(it)
                }, cartDataCallback = {
                    moveToNonVegPaymentActivity(it)
                })
            }, topBar = {
                ComposeCustomTopAppBar(modifier = Modifier, titleText = "Cart", callback = {
                    finish()
                })
            })
            LaunchedEffect(key1 = Unit, block = {
                viewModel.getLatestAddress()
            })
        }

    }

    private fun getDataFromIntent() {
        if (intent.hasExtra(NON_VEG_CART_ID)) {
            val cartId = intent.getIntExtra(NON_VEG_CART_ID, -1)
            if (cartId == -1) {
                finish()
            }
            viewModel.getNonVegCartDetails(cartId = cartId)
        } else {
            finish()
        }
    }

    private fun handleActionOfBottomAppBar(orderSummaryAction: OrderSummaryAction) {
        if (orderSummaryAction == OrderSummaryAction.UpdateOrder) {
            checkAddressThenUpdateCart()
        } else if (orderSummaryAction == OrderSummaryAction.ChangeAddress) {
            openAddressSelectionBtmSheet()
        }
    }

    private fun checkAddressThenUpdateCart() {
        viewModel.getLatestAddress()
        if (viewModel.addressItemCache == null) {
            openAddressSelectionBtmSheet()
        } else {
            viewModel.lockUserCartFinalCall()
        }
    }

    private fun openAddressSelectionBtmSheet() {
        val addressBtmSheet = AddressBottomSheetV2.newInstance()
        supportFragmentManager.showBottomSheetIsNotPresent(addressBtmSheet, "address_sheet")
    }

    private fun openAddressSearchActivity() {
        val newAddressIntent = Intent(this, AddressSearchActivity::class.java)
        startAddNewAddressActivity.launch(newAddressIntent)
    }

    private val startAddNewAddressActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedAddressId = result.data?.getIntExtra(AddNewAddressActivity.KEY_SELECTED_ADDRESS_ID, -1)
            if (selectedAddressId != null && selectedAddressId != -1) {
                checkAddressThenUpdateCart()
            }
        }
    }


    override fun didTapOnAddAddress(savedAddress: AddressItem) {
        val address = savedAddress.convertToAddress()
        viewModel.updateAddressItem(address)
        viewModel.saveLatestAddress(address)
    }

    override fun didTapOnAddNewAddress() {
        openAddressSearchActivity()
    }

    private fun moveToNonVegPaymentActivity(nonVegCartData: NonVegCartData?) {
        if (nonVegCartData == null) {
            AppUtility.showToast(this, "Something went wrong")
            return
        }
        val basicCartData = nonVegCartData.convertToBasicNonVegCartInfo()
        if (basicCartData.cartId == null) {
            return
        }
        val intent = Intent(this, NonVegPaymentActivity::class.java)
        intent.putExtra(NonVegPaymentActivity.KEY_CART_INFO, basicCartData)
        startActivity(intent)
    }
}