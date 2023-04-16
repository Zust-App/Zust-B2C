package `in`.opening.area.zustapp.repository

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.product.model.CreateCartData
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.viewmodels.ACTION
import androidx.room.Transaction
import javax.inject.Inject

open class ProductRepo @Inject constructor(
    val apiRequestManager: ApiRequestManager,
    private val dbRepo: DbAddToCartRepository,
) {

    suspend fun insertOrUpdateProduct(product: ProductSingleItem, action: ACTION) {
        dbRepo.insertOrUpdate(product, action)
    }

    suspend fun getProductListFromServer(categoryId: Int) = apiRequestManager.productListFromServer(categoryId)

    fun getAllLocalProducts() = dbRepo.getAllCartItems()

    suspend fun deleteProduct(product: ProductSingleItem) = dbRepo.deleteCartBasedOnId(product)

    suspend fun deleteAllProduct() = dbRepo.deleteAllCartItem()


}