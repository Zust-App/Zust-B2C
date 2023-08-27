package `in`.opening.area.zustapp.rapidwallet.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep


@Keep
data class RapidWalletResult(
    val orderId: Int,
    val status: Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(orderId)
        parcel.writeInt(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RapidWalletResult> {
        override fun createFromParcel(parcel: Parcel): RapidWalletResult {
            return RapidWalletResult(parcel)
        }

        override fun newArray(size: Int): Array<RapidWalletResult?> {
            return arrayOfNulls(size)
        }
    }
}
//success-->1
//failure-->-1
//pending-->0
