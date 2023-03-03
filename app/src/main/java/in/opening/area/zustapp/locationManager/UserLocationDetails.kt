package `in`.opening.area.zustapp.locationManager

data class UserLocationDetails(
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var fullAddress: String? = "",
    var city:String?="Patna"
) {
    fun copy() {
        val userLocationDetails = UserLocationDetails()
        userLocationDetails.longitude = longitude
        userLocationDetails.latitude = latitude
        userLocationDetails.fullAddress = fullAddress
        userLocationDetails.city=city
    }
}