package non_veg.payment.models

import android.os.Parcel
import android.os.Parcelable

data class PaymentCartDisplayInfoModel(
    val cartId: Int,
    val itemTotalVale: Double,
    val itemCountInCart: Int,
    val deliveryFee: Double,
    val packagingFee: Double,
    val serviceFee: Double,
    val freeDeliveryText: String? = null,
    val expectedDelivery: String? = null
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cartId)
        parcel.writeDouble(itemTotalVale)
        parcel.writeInt(itemCountInCart)
        parcel.writeDouble(deliveryFee)
        parcel.writeDouble(packagingFee)
        parcel.writeDouble(serviceFee)
        parcel.writeString(freeDeliveryText)
        parcel.writeString(expectedDelivery)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentCartDisplayInfoModel> {
        override fun createFromParcel(parcel: Parcel): PaymentCartDisplayInfoModel {
            return PaymentCartDisplayInfoModel(parcel)
        }

        override fun newArray(size: Int): Array<PaymentCartDisplayInfoModel?> {
            return arrayOfNulls(size)
        }
    }
}