package `in`.opening.area.zustapp.profile.models

import android.os.Parcel
import android.os.Parcelable
import com.google.errorprone.annotations.Keep

@Keep
data class Refer(
    val title: String? = null,
    val code: String? = null,
    val description: String? = null,
    val header: String? = null,
    val imageUrl: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(code)
        parcel.writeString(description)
        parcel.writeString(header)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Refer> {
        override fun createFromParcel(parcel: Parcel): Refer {
            return Refer(parcel)
        }

        override fun newArray(size: Int): Array<Refer?> {
            return arrayOfNulls(size)
        }
    }
}