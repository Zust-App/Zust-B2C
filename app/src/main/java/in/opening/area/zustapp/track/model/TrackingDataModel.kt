package `in`.opening.area.zustapp.tracking.track.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TrackingDataModel(val address: String? = null,
                             val currentLat: String? = null,
                             val currentLng: String? = null,
                             val targetLat: String? = null,
                             val targetLng: String? = null)