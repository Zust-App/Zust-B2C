package non_veg.storage

import androidx.room.ColumnInfo
import androidx.room.DeleteTable.Entries
import androidx.room.Embedded

import androidx.room.Entity

@Entity(tableName = "non_veg_items", primaryKeys = ["product_price_id", "merchant_id"])
data class NonVegItemLocalModel(
    @ColumnInfo(name = "product_price_id")
    val productPriceId: Int,
    @ColumnInfo(name = "merchant_id")
    val merchantId: Int,
    var quantity: Int = 0,
    var price: Double,
    var mrp: Double = -1.0,
    @ColumnInfo(name = "product_id")
    var productId: Int = -1,
    @ColumnInfo(name = "category_id")
    var categoryId: Int = -1,
)
