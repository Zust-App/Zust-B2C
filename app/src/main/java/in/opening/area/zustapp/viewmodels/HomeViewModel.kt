package `in`.opening.area.zustapp.viewmodels


import `in`.opening.area.zustapp.address.model.getDisplayString
import `in`.opening.area.zustapp.home.models.HomeData
import `in`.opening.area.zustapp.home.models.HomePageApiResponse
import `in`.opening.area.zustapp.locationManager.UserLocationDetails
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.orderDetail.models.Address
import `in`.opening.area.zustapp.product.Utils
import `in`.opening.area.zustapp.product.model.*
import `in`.opening.area.zustapp.repository.DbAddToCartRepository
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.HomePageResUi
import `in`.opening.area.zustapp.uiModels.LatestOrderUi
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiRequestManager: ApiRequestManager,
    private val dbAddToCartRepository: DbAddToCartRepository,
) : OrderSummaryNetworkVM(apiRequestManager) {
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
    internal val isAppUpdateAvail = MutableStateFlow(false)

    internal fun getUserSavedAddress() {
        val address = sharedPrefManager.getUserAddress()
        updateAddressItem(address)
    }

    internal fun getHomePageData(lat: Double?, lng: Double?) = viewModelScope.launch {
        homePageUiState.update { HomePageResUi.InitialUi(true) }
        if (lat != null && lng != null) {
            val trendingProductsResponse = apiRequestManager.getTrendingProductsWithFlow()
            val homePageResponse = apiRequestManager.getHomePageDataWithFlow(lat, lng)
            val addToCartProducts = dbAddToCartRepository.getAllCartItems()
            combine(trendingProductsResponse, homePageResponse, addToCartProducts) { trend, homePage, localProducts ->
                localProductCountMap = localProducts.associate { it.productPriceId to it.itemCountByUser }
                mergeHomePageResponse(trend, homePage)
                updateAddToCartFlow(localProducts)
            }.collect()
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
                homePageResponseCache = homePageResponse.value.data
                handleHomePageSuccessState(trendingProductsValueCache)
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
        when (val response = apiRequestManager.getLatestOrderWhichNotDelivered()) {
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
                dbAddToCartRepository.insertOrUpdate(product, action)
            }
        }
    }

    private fun updateAddToCartFlow(addToCartList: List<ProductSingleItem>) {
        val priceAndTotalItemCount = Utils.calculatePriceAndItemCount(addToCartList as ArrayList<ProductSingleItem>)
        val orderBodyRequest = addToCartList.map {
            it.convertProductToCreateOrder()
        }
        addToCartFlow.tryEmit(CreateCartReqModel(1, priceAndTotalItemCount.first,
            orderBodyRequest, priceAndTotalItemCount.second))
    }

    internal fun saveLatestAddress(address: Address) = viewModelScope.launch {
        sharedPrefManager.saveAddress(address)
    }

    private fun updateAddressItem(address: Address?) {
        userLocationFlow.update {
            UserLocationDetails(address?.latitude, address?.longitude, address?.getDisplayString())
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

    internal fun getAppMetaData() = viewModelScope.launch {
        when (val response = apiRequestManager.getMetaData()) {
            is ResultWrapper.Success -> {
                response.value.data?.let {
                    if (it.isAppUpdateAvail == true) {
                        isAppUpdateAvail.update { true }
                    }
                }
            }
            else -> {

            }
        }
    }
}
