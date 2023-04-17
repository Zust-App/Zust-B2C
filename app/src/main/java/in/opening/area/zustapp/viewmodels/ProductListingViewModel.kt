package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.product.Utils
import `in`.opening.area.zustapp.product.model.*
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.uiModels.productList.ProductListUi
import `in`.opening.area.zustapp.uiModels.productList.SingleProductUi
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListingViewModel @Inject constructor(private val productRepo: ProductRepo) : OrderSummaryNetworkVM(productRepo) {
    internal val subCategoryFlow = MutableStateFlow(SubCategoryDataMode())
    internal var headingData = MutableStateFlow("")

    internal val productListUiState = MutableStateFlow<ProductListUi>(ProductListUi.InitialUi(false))

    internal val singleProductUiState = MutableStateFlow(SingleProductUi(null, null))

    internal val categoryListUiState = MutableStateFlow<OtherCategoriesUiModel>(OtherCategoriesUiModel.InitialUi(false))

    private var allCategoryCache: ArrayList<SingleCategoryData>? = arrayListOf()

    private var selectedCategoryId: Int? = null
    private var job: Job? = null

    internal fun updateProductCount(product: ProductSingleItem?, action: ACTION) {
        product?.let {
            viewModelScope.launch {
                productRepo.insertOrUpdateProduct(it, action)
            }
        }
    }


    internal fun getMergedResponseOfProduct(categoryId: Int) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            productListUiState.update { ProductListUi.InitialUi(true) }
            val productFromServer = productRepo.getProductListFromServer(categoryId)
            val localSavedProducts = productRepo.getAllLocalProducts()
            combine(productFromServer, localSavedProducts) { serverList, localList ->
                parseProductList(serverList, localList)
                updateAddToCartFlow(localList)
            }.collect()
        }
    }

    private fun parseProductList(serverList: ResultWrapper<ProductApiResponse>, localList: List<ProductSingleItem>) {
        when (serverList) {
            is ResultWrapper.Success -> {
                if (serverList.value.data != null) {
                    compareLocalAndServerResult(serverList.value, localList)
                } else {
                    productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = serverList.value.message, errors = serverList.value.errors ?: arrayListOf()) }
                }
            }
            is ResultWrapper.GenericError -> {
                productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = serverList.error?.error) }
            }
            is ResultWrapper.NetworkError -> {
                productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = "Something went wrong") }

            }
            is ResultWrapper.UserTokenNotFound -> {
                productListUiState.update { ProductListUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
            }
        }
    }


    private fun compareLocalAndServerResult(serverProduct: ProductApiResponse, dbSavedProducts: List<ProductSingleItem>) {
        if (serverProduct.data == null) {
            productListUiState.update { ProductListUi.ErrorUi(false, errorMsg = "Something went wrong") }
            return
        }
        val localProductMap = dbSavedProducts.associate { it.productPriceId to it.itemCountByUser }

        serverProduct.data.productItems?.forEach {
            if (localProductMap.containsKey(it.productPriceId)) {
                it.itemCountByUser = localProductMap[it.productPriceId] ?: 0
            } else {
                it.itemCountByUser = 0
            }
        }

        val singleProductItem = singleProductUiState.value

        if (singleProductItem.id != null && localProductMap.containsKey(singleProductItem.id)) {
            singleProductItem.productSingleItem?.itemCountByUser = localProductMap[singleProductItem.id] ?: 0
            singleProductUiState.update { SingleProductUi(singleProductItem.productSingleItem, singleProductItem.id) }
        }
        productListUiState.update { ProductListUi.ProductListSuccess(false, serverProduct.data.copy(serverProduct.data.productItems)) }
    }

    internal fun getSubCategoryListFromServer(categoryId: Int) = viewModelScope.launch {
        val response = productRepo.apiRequestManager.getSubCategoryFromServer(categoryId)
        if (response is ResultWrapper.Success) {
            val value = response.value
            if (value.data != null) {
                subCategoryFlow.emit(SubCategoryDataMode(data = value.data))
            }
        }
    }

    internal fun updateSubCategorySelection(subCategory: SingleSubCategory?) {
        if (subCategory == null) {
            return
        }
        val updateList = subCategoryFlow.value.data?.map {
            if (subCategory.id == it.id) {
                it.copy(true)
            } else {
                it.copy(false)
            }
        }
        subCategoryFlow.tryEmit(SubCategoryDataMode(data = updateList))
    }

    private fun updateAddToCartFlow(addToCartList: List<ProductSingleItem>) {
        val priceAndTotalItemCount = Utils.calculatePriceAndItemCount(addToCartList as ArrayList<ProductSingleItem>)
        val orderBodyRequest = addToCartList.map {
            it.convertProductToCreateOrder()
        }
        addToCartFlow.tryEmit(CreateCartReqModel(-1, priceAndTotalItemCount.first, orderBodyRequest, priceAndTotalItemCount.second))
    }

    private fun getAllCategory() = viewModelScope.launch {
        categoryListUiState.update {
            OtherCategoriesUiModel.InitialUi(true)
        }
        if (allCategoryCache.isNullOrEmpty()) {
            when (val response = productRepo.apiRequestManager.getAllCategory()) {
                is ResultWrapper.Success -> {
                    allCategoryCache = response.value.data as ArrayList<SingleCategoryData>?
                    categoryListUiState.update {
                        OtherCategoriesUiModel.SuccessUi(allCategoryCache, selectedCategoryId,false)
                    }
                }
                else -> {
                    categoryListUiState.update {
                        OtherCategoriesUiModel.ErrorUi(false, null)
                    }
                }
            }
        } else {
            categoryListUiState.update {
                OtherCategoriesUiModel.SuccessUi(allCategoryCache, selectedCategoryId,false)
            }
        }
    }

    internal fun updateCategoryIdBasedOnSelection(categoryId: Int) {
        this.selectedCategoryId = categoryId
        getAllCategory()
        getMergedResponseOfProduct(categoryId)
    }

    internal fun updateHeaderData(title: String?) {
        if (title == null) {
            return
        }
        headingData.update {
            title
        }
    }
}


enum class ACTION {
    DECREASE, INCREASE
}