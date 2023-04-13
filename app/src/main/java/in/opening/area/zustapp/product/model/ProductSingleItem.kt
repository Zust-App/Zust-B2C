package `in`.opening.area.zustapp.product.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.errorprone.annotations.Keep
import com.google.gson.annotations.SerializedName


@Keep
@Entity(tableName = "cart")
data class ProductSingleItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tableId")
    var tableId: Int = 0,

    @ColumnInfo(name = "brand")
    val brand: String? = null,
    @ColumnInfo(name = "categoryId")
    val categoryId: String,
    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "productPriceId")
    @SerializedName("id")
    val productPriceId: String,

    @ColumnInfo(name = "productGroupId")
    @SerializedName("productId")
    val productGroupId: String,

    @ColumnInfo(name = "itemInStock")
    val itemInStock: Double = 0.0,
    @ColumnInfo(name = "mrp")
    val mrp: Double = -1.0,
    @ColumnInfo(name = "productName")
    @SerializedName("name")
    val productName: String = "",
    @ColumnInfo(name = "price")
    val price: Double = -1.0,
    @ColumnInfo(name = "quantity")
    val quantity: Double? = -1.0,

    @ColumnInfo(name = "quantityUnit")
    val quantityUnit: String = "KG",
    @ColumnInfo(name = "subcategoryId")
    val subcategoryId: Int,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String? = null,
    @ColumnInfo(name = "itemCountByUser")
    var itemCountByUser: Int = -1,
    @ColumnInfo(name = "discountPercentage")
    var discountPercentage: Double = -1.0,
    @ColumnInfo(name = "wareHouseId")
    var wareHouseId: String? = null,
    @ColumnInfo(name = "isOutOfStock")
    val isOutOfStock: Boolean = false,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()?:"",
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()?:"",
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(tableId)
        parcel.writeString(brand)
        parcel.writeString(categoryId)
        parcel.writeString(description)
        parcel.writeString(productPriceId)
        parcel.writeString(productGroupId)
        parcel.writeDouble(itemInStock)
        parcel.writeDouble(mrp)
        parcel.writeString(productName)
        parcel.writeDouble(price)
        parcel.writeValue(quantity)
        parcel.writeString(quantityUnit)
        parcel.writeInt(subcategoryId)
        parcel.writeString(thumbnail)
        parcel.writeInt(itemCountByUser)
        parcel.writeDouble(discountPercentage)
        parcel.writeString(wareHouseId)
        parcel.writeByte(if (isOutOfStock) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductSingleItem> {
        override fun createFromParcel(parcel: Parcel): ProductSingleItem {
            return ProductSingleItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductSingleItem?> {
            return arrayOfNulls(size)
        }
    }
    fun copy(userSelectedItemCount: Int): ProductSingleItem {
        return ProductSingleItem(tableId, brand, categoryId,
            description,
            productPriceId, productGroupId, itemInStock,
            mrp, productName,
            price, quantity, quantityUnit, subcategoryId, thumbnail,
            userSelectedItemCount,
            discountPercentage, wareHouseId, isOutOfStock)
    }
}


fun ProductSingleItem.convertProductToCreateOrder(): CreateCartReqItem {
    return CreateCartReqItem(mrp = mrp, numberOfItem = itemCountByUser, productId = productPriceId, payablePrice = price)
}

fun ProductSingleItem.copy(): ProductSingleItem {
    return ProductSingleItem(tableId, brand, categoryId,
        description,
        productPriceId, productGroupId,
        itemInStock,
        mrp, productName,
        price, quantity, quantityUnit,
        subcategoryId, thumbnail,
        itemCountByUser,
        discountPercentage,
        wareHouseId, isOutOfStock)
}

fun ProductSingleItem?.validateProduct(): Boolean {
    if (this == null) {
        return false
    }
    if (this.isOutOfStock) {
        return false
    }
    return true
}
