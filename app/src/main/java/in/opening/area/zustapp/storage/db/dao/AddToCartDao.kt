package `in`.opening.area.zustapp.storage.db.dao

import `in`.opening.area.zustapp.product.model.ProductSingleItem
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface AddToCartDao {
    companion object {
        private const val query_select_all = "select * from cart"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleCart(product: ProductSingleItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartList(products: List<ProductSingleItem>)

    @Query(query_select_all)
    fun getAllCartItem(): Flow<List<ProductSingleItem>>

    @Query(value = "UPDATE cart SET itemCountByUser=:count WHERE productPriceId = :productId")
    suspend fun updateCartItemCountBasedOnPid(productId: String, count: Int)

    @Query(value = "DELETE FROM cart WHERE productPriceId = :productId")
    suspend fun deleteItem(productId: String): Int

    @Query(value = "SELECT productPriceId FROM cart WHERE productPriceId = :productId LIMIT 1")
    suspend fun checkBasedOnProductId(productId: String?): String?

    @Query(value = "DELETE FROM cart")
    suspend fun deleteAllCartItem()

    @Query(value = "UPDATE cart SET itemCountByUser=itemCountByUser+1 WHERE productPriceId = :productId")
    suspend fun increaseUserProductCount(productId: String)

    @Query(value = "UPDATE cart SET itemCountByUser=itemCountByUser-1 WHERE productPriceId = :productId")
    suspend fun decreaseUserProductCount(productId: String)

    @Transaction
    suspend fun insertOrUpdate(addToCartModel: ProductSingleItem) {
        val productId = checkBasedOnProductId(addToCartModel.productPriceId)
        if (productId != null) {
            updateCartItemCountBasedOnPid(productId, addToCartModel.itemCountByUser)
        } else {
            insertSingleCart(addToCartModel)
        }
    }
    @Query(query_select_all)
    fun getAllCartItemWithoutFlow(): List<ProductSingleItem>

}
