package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.orderSummary.model.CancellationPolicyUiModel
import `in`.opening.area.zustapp.orderSummary.model.LockOrderSummaryItem
import `in`.opening.area.zustapp.orderSummary.model.LockOrderSummaryModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.orderSummary.LockOrderCartUi
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummary
import `in`.opening.area.zustapp.uiModels.orderSummary.OrderSummaryUi
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

val IO = Dispatchers.IO

@HiltViewModel
class OrderSummaryViewModel @Inject constructor(
    private val apiRequestManager: ApiRequestManager,
    private val productRepo: ProductRepo,
) : OrderSummaryNetworkVM(apiRequestManager) {

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    private var deliveryPartnerTipAmount: Int = 0

    internal val addedCartUiState = MutableStateFlow<OrderSummaryUi>(OrderSummaryUi.InitialUi(false))
    private val lockOrderSummaryItems = ArrayList<LockOrderSummaryItem>()
    private val cartItemsIdMap = HashMap<String, Int>()

    internal val expectedDeliveryTimeUiState = MutableStateFlow<String?>(String())

    internal val lockedCartUiState = MutableStateFlow<LockOrderCartUi>(LockOrderCartUi.InitialUi(false))

    internal val deliveryPartnerTipUiState = MutableStateFlow<DeliveryPartnerTipUiState>(DeliveryPartnerTipUiState.Success(deliveryPartnerTipAmount, isLoading = false))

    //cache data
    internal val cancellationPolicyCacheData = MutableStateFlow<CancellationPolicyUiModel>(CancellationPolicyUiModel.InitialUi(false))

    internal var addressItemCache: Address? = null
    private var upSellingProductsCache: ArrayList<ProductSingleItem>? = null
    private val originalCartItems = ArrayList<ProductSingleItem>()
    private var upSellingItemsId: Set<String> = hashSetOf()
    internal val addressLineData = MutableStateFlow("")


    init {
        cancellationPolicyCacheData.update {
            CancellationPolicyUiModel.CancellationPolicyUiSuccess(false, "Cancellation policy")
        }
    }

    internal fun getCartItemsFromDb() = viewModelScope.launch(IO) {
        addedCartUiState.update {
            OrderSummaryUi.InitialUi(false)
        }
        productRepo.getAllLocalProducts().stateIn(viewModelScope).collectLatest { list ->
            if (upSellingProductsCache == null) {
                getUpsellingProducts(list.joinToString(separator = ",") { it.productGroupId })
            }
            proceedWithCollectedResponse(list)
        }
    }

    private fun proceedWithCollectedResponse(productSingleItems: List<ProductSingleItem>) {
        lockOrderSummaryItems.clear()
        originalCartItems.clear()
        cartItemsIdMap.clear()
        var selectedItemCount = 0
        var totalOriginalPrice = 0.0
        var totalCurrentPrice = 0.0
        productSingleItems.forEach {
            cartItemsIdMap[it.productPriceId] = it.itemCountByUser
            selectedItemCount += it.itemCountByUser
            totalOriginalPrice += (it.mrp) * it.itemCountByUser
            totalCurrentPrice += (it.price) * it.itemCountByUser

            lockOrderSummaryItems.add(LockOrderSummaryItem(it.mrp,
                it.itemCountByUser, it.price,
                it.productPriceId.toInt()))
            if (!upSellingItemsId.contains(it.productPriceId)) {
                originalCartItems.add(it)
            }
        }
        upSellingProductsCache?.forEach {
            if (cartItemsIdMap.containsKey(it.productPriceId)) {
                it.itemCountByUser = cartItemsIdMap[it.productPriceId] ?: 0
            } else {
                it.itemCountByUser = 0
            }
        }

        addedCartUiState.update {
            OrderSummaryUi.SummarySuccess(false,
                data = OrderSummary(originalCartItems,
                    selectedItemCount,
                    totalOriginalPrice,
                    totalCurrentPrice,
                    suggestedProducts = upSellingProductsCache))
        }
    }

    internal fun increaseItemCount(product: ProductSingleItem) = viewModelScope.launch(IO) {
        productRepo.insertOrUpdateProduct(product, ACTION.INCREASE)
    }

    internal fun decreaseItemCount(product: ProductSingleItem) = viewModelScope.launch(IO) {
        productRepo.insertOrUpdateProduct(product, ACTION.DECREASE)
    }

    internal fun deleteItem(product: ProductSingleItem) = viewModelScope.launch {
        productRepo.deleteProduct(product)
    }


    internal fun updateUserCartWithServer(orderId: Int) = viewModelScope.launch {
        lockedCartUiState.update { LockOrderCartUi.InitialUi(true) }
        if (addressItemCache == null) {
            lockedCartUiState.update { LockOrderCartUi.ErrorUi(false, errorMessage = "Please select address") }
            return@launch
        }
        if (lockOrderSummaryItems.isEmpty()) {
            lockedCartUiState.update { LockOrderCartUi.ErrorUi(false, errorMessage = "May be something wrong with cart") }
            return@launch
        } else {
            val lockOrderSummaryModel = LockOrderSummaryModel(addressId = addressItemCache!!.id,
                lockOrderSummaryItems = lockOrderSummaryItems,
                merchantId = 6, orderId = orderId, deliveryPartnerTip = deliveryPartnerTipAmount)
            when (val response = apiRequestManager.syncUserCartWithServerAndLock(lockOrderSummaryModel)) {
                is ResultWrapper.Success -> {
                    if (response.value.lockOrderResponseData != null) {
                        lockedCartUiState.update {
                            saveLatestAddress(response.value.lockOrderResponseData.address)
                            LockOrderCartUi.SuccessLocked(false, data = response.value.lockOrderResponseData)
                        }
                    } else {
                        lockedCartUiState.update { LockOrderCartUi.ErrorUi(false, errorMessage = response.value.message, errors = response.value.errors) }
                    }
                }
                is ResultWrapper.NetworkError -> {
                    lockedCartUiState.update { LockOrderCartUi.ErrorUi(false, errorMessage = "Something went wrong") }
                }
                is ResultWrapper.GenericError -> {
                    lockedCartUiState.update { LockOrderCartUi.ErrorUi(false, errorMessage = response.error?.error ?: "Something went wrong") }
                }
                is ResultWrapper.UserTokenNotFound -> {
                    lockedCartUiState.update { LockOrderCartUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
                }
            }

        }
    }

    internal fun isLockedCartOnGoing(): Boolean {
        return lockedCartUiState.value.isLoading
    }

    internal fun updateDeliveryPartnerTip(amount: Int? = null) {
        if (amount == null) {
            deliveryPartnerTipAmount = 0
            return
        } else {
            deliveryPartnerTipAmount = amount
            deliveryPartnerTipUiState.update {
                DeliveryPartnerTipUiState.Success(isLoading = false, selectedAmount = deliveryPartnerTipAmount)
            }
        }
        val alreadyCartData = addedCartUiState.value
        if (deliveryPartnerTipAmount == amount) {
            if (alreadyCartData is OrderSummaryUi.SummarySuccess) {
                addedCartUiState.update {
                    OrderSummaryUi.SummarySuccess(false, data = alreadyCartData.data.copy(deliveryPartnerTipAmount))
                }
            }
        }
    }

    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    internal fun getLatestAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
    }

    internal fun updateAddressItem(address: Address?) {
        this.addressItemCache = address
        addressLineData.update {
            address?.getDisplayString() ?: ""
        }
    }

    private fun getUpsellingProducts(params: String) = viewModelScope.launch {
        when (val response = apiRequestManager.getUpsellingProducts(params)) {
            is ResultWrapper.Success -> {
                if (response.value.data?.productItems?.isEmpty() == false) {
                    upSellingProductsCache = (response.value.data.productItems as ArrayList<ProductSingleItem>?)!!
                    upSellingItemsId = upSellingProductsCache!!.map { it.productPriceId }.toSet()

                    val alreadyCartData = addedCartUiState.value
                    if (alreadyCartData is OrderSummaryUi.SummarySuccess) {
                        addedCartUiState.update {
                            OrderSummaryUi.SummarySuccess(false, data = alreadyCartData.data.copy(upSellingProductsCache))
                        }
                        OrderSummaryUi.SummarySuccess(false, data = OrderSummary().copy())
                    }
                }
            }
            else -> {

            }

        }
    }


}

interface DeliveryPartnerTipUiState {
    val isLoading: Boolean

    data class Success(
        val selectedAmount: Int? = null,
        val amountList: ArrayList<Int> = arrayListOf(5, 10, 15, 20),
        val time: Long = System.currentTimeMillis(),
        override val isLoading: Boolean,
    ) : DeliveryPartnerTipUiState

    data class InitialUi(override val isLoading: Boolean) : DeliveryPartnerTipUiState
}


