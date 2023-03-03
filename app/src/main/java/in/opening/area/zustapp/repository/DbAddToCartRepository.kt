package `in`.opening.area.zustapp.repository

import `in`.opening.area.zustapp.abstraction.TransactionRunnerDao
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.storage.db.dao.AddToCartDao
import `in`.opening.area.zustapp.viewmodels.ACTION
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbAddToCartRepository @Inject constructor(private var addToCartDao: AddToCartDao) : TransactionRunnerDao {

    fun getAllCartItems() = addToCartDao.getAllCartItem()


    private suspend fun insertSingleProduct(addToCartModel: ProductSingleItem) {
        addToCartDao.insertSingleCart(addToCartModel)
    }

    suspend fun deleteCartBasedOnId(item: ProductSingleItem): Int {
        return addToCartDao.deleteItem(item.productPriceId)
    }

    suspend fun deleteAllCartItem() {
        addToCartDao.deleteAllCartItem()
    }

    suspend fun insertOrUpdate(product: ProductSingleItem, action: ACTION) {
        runInTransaction {
            if (checkProductAlreadyExist(product)) {
                if (action == ACTION.INCREASE) {
                    increaseCount(product.productPriceId)
                } else if (action == ACTION.DECREASE) {
                    if (product.itemCountByUser - 1 == 0) {
                        deleteCartBasedOnId(product)
                    } else {
                        decreaseCount(product.productPriceId)
                    }
                }
            } else {
                val copiedProduct = product.copy()
                copiedProduct.itemCountByUser = 1
                insertSingleProduct(copiedProduct)
            }
        }
    }

    private suspend fun checkProductAlreadyExist(product: ProductSingleItem): Boolean {
        val productId: String? = addToCartDao.checkBasedOnProductId(product.productPriceId)
        if (productId != null) {
            return true
        }
        return false
    }

    private suspend fun increaseCount(productId: String) = addToCartDao.increaseUserProductCount(productId)
    private suspend fun decreaseCount(productId: String) = addToCartDao.decreaseUserProductCount(productId)

}