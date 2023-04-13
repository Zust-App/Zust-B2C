package `in`.opening.area.zustapp.data

import `in`.opening.area.zustapp.utility.UserCustomError

data class AppMetaDataResponse(
    val `data`: AppMetaData? = null,
    val errors: List<UserCustomError>? = null,
    val message: String? = null,
    val statusCode: Int? = -1,
)

data class AppMetaData(
    val cancellationPolicy: CancellationPolicy? = null,
    val deliveryFeePolicy: DeliveryFeePolicy? = null,
    val deliveryTip: DeliveryTip? = null,
    val projectBaseUrl: String,
    val testBaseUrl: String,
    val isAppUpdateAvail: Boolean? = false,
    val freeDeliveryFee: Double? = 99.0,
)

data class DeliveryFeePolicy(
    val deliveryFeePolicyEn: String,
    val deliveryFeePolicyHi: String,
)

data class DeliveryTip(
    val header: String,
    val sub_header: String,
    val tip_Price: List<Int>,
)

data class CancellationPolicy(
    val cancellationPolicyEn: String,
    val cancellationPolicyHi: String,
)