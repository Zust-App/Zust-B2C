package zustbase.services.models

import `in`.opening.area.zustapp.utility.UserCustomError

data class ZustAvailableServiceResult(
    val `data`: ZustServiceData? = null,
    val errors: List<UserCustomError>?,
    val message: String,
    val statusCode: Int,
    val timestamp: String,
)

data class ZustServiceData(
    val serviceList: List<ZustService>?
)

data class ZustService(
    val description: String?,
    val enable: Boolean,
    val imageUrl: String?,
    val type: String,
)