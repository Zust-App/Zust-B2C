package non_veg.product_details.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import kotlinx.coroutines.launch
import non_veg.cart.viewmodel.NonVegCartViewModel
import non_veg.storage.dao.NonVegAddToCartDao
import javax.inject.Inject

@HiltViewModel
class NvProductDetailsViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager, private val nonVegAddToCartDao: NonVegAddToCartDao) : NonVegCartViewModel(apiRequestManager, nonVegAddToCartDao) {
    private var nonVegMerchantId: Int = -1
    internal var productId: Int = -1
    internal var productPriceId: Int = -1

    internal fun getNonVegProductDetail() = viewModelScope.launch {
        if (nonVegMerchantId == -2) {
            return@launch
        }
        if (nonVegMerchantId == -1) {
            nonVegMerchantId = sharedPrefManager.getNonVegMerchantId()
        }
        if (nonVegMerchantId == -1) {
            nonVegMerchantId = -2
            return@launch
        }
        if (productId == -1 && productPriceId == -1) {
            return@launch
        }
        when (val response = apiRequestManager.getNonVegProductDetails(productPriceId = productPriceId, productId = productId, merchantId = nonVegMerchantId)) {

        }
    }
}