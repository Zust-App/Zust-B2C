package `in`.opening.area.zustapp.locationManager.models

data class CustomLocationModel(
    var addressLine: String?=null,
    var pinCode: String?=null,
    var lat: Double?=0.0,
    var lng: Double?=0.0,
    var city: String?=null,
    var state:String?=null,
    var country: String? = null,
    var knownName: String? = null,
)
