package zustbase.basepage.models


data class ZustServicePageResponseReceiver(val data: ZustServicePageResponse? = null)
data class ZustServicePageResponse(val data: List<ServicePageData>? = null)
data class ServicePageData(val key: String, val title: String, val list: List<ServicePageSingleItemData>?)
data class ServicePageSingleItemData(val imageUrl: String?, val deepLink: String?)