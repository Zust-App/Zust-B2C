package `in`.opening.area.zustapp.viewmodels


import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.home.models.HomeData
import `in`.opening.area.zustapp.home.models.HomePageApiResponse
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ResultWrapper
import zustbase.orderDetail.models.ZustAddress
import `in`.opening.area.zustapp.product.Utils
import `in`.opening.area.zustapp.product.model.*
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.HomePageResUi
import `in`.opening.area.zustapp.uiModels.LatestOrderUi
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager.Companion.NOT_COVERAGE_ERROR_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryHomeViewModel @Inject constructor(
    private val productRepo: ProductRepo,
) : OrderSummaryNetworkVM(productRepo) {
    internal val userLocationFlow = MutableStateFlow(UserLocationDetails())
    internal val homePageUiState = MutableStateFlow<HomePageResUi>(HomePageResUi.InitialUi(false))
    internal val latestOrderUiState = MutableStateFlow<LatestOrderUi>(LatestOrderUi.InitialUi(false))

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    private var trendingProductsValueCache: ProductItemResponse? = null

    private var homePageResponseCache: HomeData? = null

    private var localProductCountMap: Map<String, Int> = mapOf()

    internal val moveToLoginPage = MutableStateFlow(false)
    private var merchantId: Int? = -1
    private var job: Job? = null
    internal fun getUserSavedAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
        address?.let {
            if (address.latitude != null && address.latitude != 0.0) {
                getGroceryHomePageData(address.latitude, address.longitude,address.pinCode)
            } else {
                homePageUiState.update { HomePageResUi.ErrorUi(false, errorMsg = "Invalid address please try other location", errors = arrayListOf(), errorCode = NOT_COVERAGE_ERROR_CODE) }
            }
        }
    }

    private fun getGroceryHomePageData(lat: Double?, lng: Double?,pinCode:String?) {
        job?.cancel()
        job = viewModelScope.launch {
            homePageUiState.update { HomePageResUi.InitialUi(true) }
            if (lat != null && lng != null) {
                val trendingProductsResponse = productRepo.apiRequestManager.getTrendingProductsWithFlow(lat, lng,pinCode)
                val homePageResponse = productRepo.apiRequestManager.getHomePageDataWithFlow(lat, lng,pinCode)
                val addToCartProducts = productRepo.dbRepo.getAllCartItems()
                combine(trendingProductsResponse, homePageResponse, addToCartProducts) { trend, homePage, localProducts ->
                    localProductCountMap = localProducts.associate { it.productPriceId to it.itemCountByUser }
                    mergeHomePageResponse(trend, homePage)
                    updateAddToCartFlow(localProducts)
                }.collect()
            }
        }
    }

    private fun mergeHomePageResponse(
        trendingResponse: ResultWrapper<ProductApiResponse>,
        homePageResponse: ResultWrapper<HomePageApiResponse>,
    ) {
        if (trendingResponse is ResultWrapper.Success) {
            if (trendingResponse.value.statusCode == 401) {
                moveToLoginPage.update { true }
                return
            }
            trendingProductsValueCache = trendingResponse.value.data
            trendingProductsValueCache?.productItems?.forEach {
                if (localProductCountMap.containsKey(it.productPriceId)) {
                    it.itemCountByUser = localProductCountMap[it.productPriceId] ?: 0
                } else {
                    it.itemCountByUser = 0
                }
            }
        } else if (trendingResponse is ResultWrapper.GenericError) {
            if (trendingResponse.code == 401) {
                moveToLoginPage.update { true }
                return
            }
        }

        when (homePageResponse) {
            is ResultWrapper.Success -> {
                if (homePageResponse.value.statusCode == 401) {
                    moveToLoginPage.update { true }
                    return
                }
                if (!homePageResponse.value.errors.isNullOrEmpty()) {
                    homePageUiState.update { HomePageResUi.ErrorUi(false, errorMsg = homePageResponse.value.message, errors = homePageResponse.value.errors, errorCode = NOT_COVERAGE_ERROR_CODE) }
                    return
                }
                homePageResponseCache = homePageResponse.value.data
                handleHomePageSuccessState(trendingProductsValueCache)
                if (merchantId != homePageResponse.value.data?.merchantId) {
                    sharedPrefManager.saveMerchantId(homePageResponse.value.data?.merchantId)
                }
            }

            is ResultWrapper.GenericError -> {
                if (homePageResponse.code == 401) {
                    moveToLoginPage.update { true }
                    return
                }
                homePageUiState.update { HomePageResUi.ErrorUi(false, errorMsg = homePageResponse.error?.error) }
            }

            is ResultWrapper.NetworkError -> {
                homePageUiState.update { HomePageResUi.ErrorUi(false, errorMsg = "Something went wrong") }
            }

            is ResultWrapper.UserTokenNotFound -> {
                moveToLoginPage.update { true }
                homePageUiState.update { HomePageResUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
        }
    }

    private fun handleHomePageSuccessState(trendingProductsValue: ProductItemResponse?) {
        trendingProductsValueCache = trendingProductsValue?.copy(trendingProductsValue.productItems)
        homePageUiState.update { HomePageResUi.HomeSuccess(homePageResponseCache, trendingProductsValueCache, false) }
    }


    internal fun getLatestOrderNotDeliver() = viewModelScope.launch {
        latestOrderUiState.update {
            LatestOrderUi.InitialUi(true)
        }
        when (val response = productRepo.apiRequestManager.getLatestOrderWhichNotDelivered()) {
            is ResultWrapper.Success -> {
                if (response.value.statusCode == 401) {
                    moveToLoginPage.update { true }
                    return@launch
                } else if (response.value.data != null) {
                    latestOrderUiState.update {
                        LatestOrderUi.LOrderSuccess(false, response.value.data)
                    }
                } else {
                    latestOrderUiState.update {
                        LatestOrderUi.ErrorUi(false, canRemove = true)
                    }
                }
            }

            else -> {
                latestOrderUiState.update {
                    LatestOrderUi.ErrorUi(false, canRemove = true)
                }
            }
        }
    }

    internal fun updateOrInsertItems(product: ProductSingleItem?, action: ACTION) {
        if (product == null) {
            return
        } else {
            viewModelScope.launch {
                productRepo.dbRepo.insertOrUpdate(product, action)
            }
        }
    }

    private fun updateAddToCartFlow(addToCartList: List<ProductSingleItem>) {
        val priceAndTotalItemCount = Utils.calculatePriceAndItemCount(addToCartList as ArrayList<ProductSingleItem>)
        val orderBodyRequest = addToCartList.map {
            it.convertProductToCreateOrder()
        }
        addToCartFlow.tryEmit(CreateCartReqModel(-1, priceAndTotalItemCount.first,
            orderBodyRequest, priceAndTotalItemCount.second))
    }

    internal fun saveLatestAddress(zustAddress: ZustAddress) = viewModelScope.launch {
        sharedPrefManager.saveAddress(zustAddress)
    }

    private fun updateAddressItem(zustAddress: ZustAddress?) {
        userLocationFlow.update {
            UserLocationDetails(zustAddress?.latitude, zustAddress?.longitude, zustAddress?.getDisplayString())
        }
    }

    internal fun removeUserLocalData() = viewModelScope.launch {
        sharedPrefManager.apply {
            removeAuthToken()
            removeSavedAddress()
            removeIsProfileCreated()
            removePhoneNumber()
        }
    }

    internal fun clearAllDataFromCart() = viewModelScope.launch(Dispatchers.IO) {
        productRepo.deleteAllProduct()
    }
}
