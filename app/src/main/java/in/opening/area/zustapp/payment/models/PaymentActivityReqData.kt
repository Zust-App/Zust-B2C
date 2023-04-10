package `in`.opening.area.zustapp.payment.models

import android.os.Parcel
import android.os.Parcelable


data class PaymentActivityReqData(var orderId: Int? = -1,
                                  var itemPrice: Double? = 0.0,
                                  var deliveryFee: Double? = 0.0,
                                  var packagingFee: Double? = 0.0,
                                  var couponDiscount: Double? = 0.0,
                                  var totalAmount: Double? =0.0,
                                  var couponString: String?=null,
                                  var deliveryPartnerTip:Double?=0.0,
                                  var isFreeDelivery:Boolean?=null,
                                  var expectedDelivery:String?=null,
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(orderId)
        parcel.writeValue(itemPrice)
        parcel.writeValue(deliveryFee)
        parcel.writeValue(packagingFee)
        parcel.writeValue(couponDiscount)
        parcel.writeValue(totalAmount)
        parcel.writeString(couponString)
        parcel.writeValue(deliveryPartnerTip)
        parcel.writeValue(isFreeDelivery)
        parcel.writeString(expectedDelivery)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentActivityReqData> {
        override fun createFromParcel(parcel: Parcel): PaymentActivityReqData {
            return PaymentActivityReqData(parcel)
        }

        override fun newArray(size: Int): Array<PaymentActivityReqData?> {
            return arrayOfNulls(size)
        }
    }
}