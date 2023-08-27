package zustbase.basepage.models

import `in`.opening.area.zustapp.utility.UserCustomError


data class ZustServicePageResponseReceiver(val data: ZustServicePageResponse? = null, val errors: List<UserCustomError>? = null,
                                           val message: String? = null,
                                           val statusCode: Int? = -1,)
data class ZustServicePageResponse(val data: List<ServicePageData>? = null)
data class ServicePageData(val key: String, val title: String, val list: List<ServicePageSingleItemData>?)
data class ServicePageSingleItemData(val imageUrl: String?, val deepLink: String?)