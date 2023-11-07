package `in`.opening.area.zustapp.locationV2.models

import android.os.Parcel
import android.os.Parcelable
import com.google.errorprone.annotations.Keep
import `in`.opening.area.zustapp.address.model.SearchAddressModel

@Keep
@androidx.annotation.Keep
data class ApartmentListingModelResponse(
    val `data`: List<ApartmentData>? = arrayListOf(),
    val error: String? = null,
    val errors: List<String>? = null,
    val status: String,
    val statusCode: Int,
)

@Keep
@androidx.annotation.Keep
data class ApartmentData(
    val address: String?,
    val apartmentName: String?,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val pinCode: String?,
    val isEnable: Boolean? = true,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(apartmentName)
        parcel.writeInt(id)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(pinCode)
        parcel.writeValue(isEnable)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ApartmentData> {
        override fun createFromParcel(parcel: Parcel): ApartmentData {
            return ApartmentData(parcel)
        }

        override fun newArray(size: Int): Array<ApartmentData?> {
            return arrayOfNulls(size)
        }
    }

}
fun ApartmentData.convertToAddressModel(): SearchAddressModel {
   return SearchAddressModel(this.latitude,this.longitude,this.address,this.pinCode,this,true)
}
