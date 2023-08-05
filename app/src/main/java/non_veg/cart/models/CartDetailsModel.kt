package non_veg.cart.models

import android.os.Parcel
import android.os.Parcelable
import `in`.opening.area.zustapp.utility.UserCustomError

data class NonVegCartDetailsModel(
    val `data`: NonVegCartData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String,
    val statusCode: Int,
)

data class NonVegCartData(
    val cartId: Int? = null,
    val deliveryFee: Double? = null,
    val itemPrice: Double? = null,
    val itemsInCart: List<ItemsInCart>? = null,
    val merchantId: Int? = null,
    val packagingFee: Double? = null,
    val serviceCharge: Double? = null,
    val noOfItemsInCart: Int? = null,
    val expectedDeliveryTime: String? = null,
    val freeDeliveryMessage: String? = null,
)

data class NonVegCartDetailsForPayment(
    val cartId: Int? = null,
    val deliveryFee: Double? = null,
    val itemPrice: Double? = null,
    val merchantId: Int? = null,
    val packagingFee: Double? = null,
    val serviceCharge: Double? = null,
    val noOfItemsInCart: Int? = null,
    val expectedDeliveryTime: String? = null,
    val freeDeliveryMessage: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(cartId)
        parcel.writeValue(deliveryFee)
        parcel.writeValue(itemPrice)
        parcel.writeValue(merchantId)
        parcel.writeValue(packagingFee)
        parcel.writeValue(serviceCharge)
        parcel.writeValue(noOfItemsInCart)
        parcel.writeString(expectedDeliveryTime)
        parcel.writeString(freeDeliveryMessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NonVegCartDetailsForPayment> {
        override fun createFromParcel(parcel: Parcel): NonVegCartDetailsForPayment {
            return NonVegCartDetailsForPayment(parcel)
        }

        override fun newArray(size: Int): Array<NonVegCartDetailsForPayment?> {
            return arrayOfNulls(size)
        }
    }
}

fun NonVegCartData.convertToBasicNonVegCartInfo(): NonVegCartDetailsForPayment {
    return NonVegCartDetailsForPayment(this.cartId, this.deliveryFee,
        this.itemPrice,
        this.merchantId,
        this.packagingFee,
        this.serviceCharge, this.noOfItemsInCart, this.expectedDeliveryTime, this.freeDeliveryMessage)
}