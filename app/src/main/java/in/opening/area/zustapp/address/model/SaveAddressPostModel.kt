package `in`.opening.area.zustapp.address.model

import zustbase.orderDetail.models.ZustAddress
import `in`.opening.area.zustapp.utility.UserCustomError
import android.os.Parcel
import android.os.Parcelable
import com.google.errorprone.annotations.Keep

@Keep
data class SaveAddressPostModel(
    var pinCode: String? = null,
    var houseNumberAndFloor: String? = null,
    var landmark: String? = null,
    var description: String? = "",
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var addressType: String? = "HOME",
    var optionalMobileNumber: String? = "",
)


@Keep
data class CustomAddress(
    var houseNumberAndFloor: String? = null,
    var addressType: String? = "HOME",
    var landmark: String? = null,
    var description: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var pinCode: String? = null,
    var id: Int? = -1,
) {

    fun convertToAddressItem(): ZustAddress {
        return ZustAddress(null,
            addressType, description,
            houseNumberAndFloor,
            id ?: -1,
            landmark,
            latitude,
            longitude, pinCode = pinCode)
    }

}


@Keep
data class SaveAddressPostResponse(
    val data: CustomAddress? = null,
    val message: String = "",
    val errors: List<UserCustomError> = arrayListOf(),
)


@Keep
data class SearchAddressModel(
    val lat: Double? = null,
    val longitude: Double? = null,
    val addressDesc: String? = null, val postalCode: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(lat)
        parcel.writeValue(longitude)
        parcel.writeString(addressDesc)
        parcel.writeString(postalCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchAddressModel> {
        override fun createFromParcel(parcel: Parcel): SearchAddressModel {
            return SearchAddressModel(parcel)
        }

        override fun newArray(size: Int): Array<SearchAddressModel?> {
            return arrayOfNulls(size)
        }
    }


}