package non_veg.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import non_veg.storage.NonVegItemLocalModel

@Dao
interface NonVegAddToCartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNonVegItemIntoLocal(item: NonVegItemLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNonVegItemIntoLocal(items: List<NonVegItemLocalModel>)

    @Query("SELECT * FROM non_veg_items WHERE merchant_id = :merchantId")
    fun getAllNonVegItemFromLocal(merchantId: Int): Flow<List<NonVegItemLocalModel>>

    @Query("SELECT * FROM non_veg_items WHERE merchant_id = :merchantId")
    suspend fun getAllNonVegItemFromLocals(merchantId: Int): List<NonVegItemLocalModel>

    @Query("DELETE FROM non_veg_items WHERE merchant_id = :merchantId and product_price_id=:productPriceId")
    fun deleteNonVegSingleCartItem(merchantId: Int, productPriceId: Int)

    @Query("DELETE FROM non_veg_items")
    fun deleteAllNonVegCartItems()

    @Query("SELECT * FROM non_veg_items WHERE product_price_id IN (:productPriceIds) AND merchant_id = :merchantId")
    fun getNonVegProductsByPPIds(productPriceIds: List<Int>, merchantId: Int): Flow<List<NonVegItemLocalModel>>

    @Query("SELECT * FROM non_veg_items WHERE product_price_id IN (:productIds) AND merchant_id = :merchantId")
    fun getNonVegProductsByPIds(productIds: List<Int>, merchantId: Int): Flow<List<NonVegItemLocalModel>>

    @Transaction
    suspend fun deleteAllAndInsertAll(items: List<NonVegItemLocalModel>) {
        deleteAllNonVegCartItems()
        insertAllNonVegItemIntoLocal(items)
    }
}