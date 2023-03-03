package `in`.opening.area.zustapp.address.model

data class DeliverablePinCodeModel(
    val pincode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)

data class DeliverableAddressResponse(
    val data: DeliverableAddress? = null,
    val message:String?=null
)

data class DeliverableAddress(val isDeliverablePinCode: Boolean)