package `in`.opening.area.zustapp.address.model

import zustbase.orderDetail.models.ZustAddress
import `in`.opening.area.zustapp.utility.PagingMetadata
import `in`.opening.area.zustapp.utility.UserCustomError
import com.google.errorprone.annotations.Keep

@Keep
data class GetUserAddressModel(
    val `data`: AddressData? = null,
    val errors: List<UserCustomError> = arrayListOf(),
    val message: String? = null,
    val statusCode: Int? = -1,
    val timeStamp: String? = null,
)

@Keep
data class AddressData(
    val _metadata: PagingMetadata,
    val addresses: List<AddressItem>,
)

@Keep
data class AddressItem(
    val addressLevel: String?,
    val addressType: String?,
    val description: String?,
    val houseNumberAndFloor: String?,
    val id: Int,
    val landmark: String?,
    val latitude: Double?,
    val longitude: Double?,
    val pinCode: String?,
    val is_high_priority: Boolean? = false,
) {
    fun convertToAddress(): ZustAddress {
        return ZustAddress(addressLevel,
            addressType,
            description,
            houseNumberAndFloor,
            id, landmark, latitude, longitude,
            pinCode = pinCode, is_high_priority = is_high_priority)
    }
}

fun ZustAddress.getDisplayString(): String {
    return buildString {
        if (!houseNumberAndFloor.isNullOrEmpty()) {
            append(houseNumberAndFloor)
            append(",")
        }
        if (!landmark.isNullOrEmpty()) {
            append(landmark)
            append(",")
        }
        if (!description.isNullOrEmpty()) {
            append(description)
        }
    }
}

fun AddressItem.getDisplayString(): String {
    return buildString {
        if (!houseNumberAndFloor.isNullOrEmpty()) {
            append(houseNumberAndFloor)
            append(",")
        }
        if (!landmark.isNullOrEmpty()) {
            append(landmark)
            append(",")
        }
        if (!description.isNullOrEmpty()) {
            append(description)
        }
    }
}

