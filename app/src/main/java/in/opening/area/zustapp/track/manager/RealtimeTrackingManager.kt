package `in`.opening.area.zustapp.tracking.track.manager

import `in`.opening.area.zustapp.tracking.track.model.TrackingDataModel
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RealtimeTrackingManager(private var realtimeTrackingManager: RealtimeTrackingInterface) {
    private val TAG = "firebase"
    private var orderId: String? = null
    private var cityName: String? = "Patna"
    private val database = Firebase.database


    internal fun setOrderId(orderId: String?, cityName: String?) {
        this.orderId = orderId
        this.cityName = cityName
    }


    internal fun startObservingPartnerLocation() {
        if (orderId == null || cityName == null) {
            return
        } else {
            database.getReference(getTrackingPath()).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        val trackingDataModel = dataSnapshot.getValue<TrackingDataModel>()
                        realtimeTrackingManager.getUpdatedLocation(trackingDataModel)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Failed to read value.", error.toException())
                }
            })
        }
    }

    private fun getTrackingPath(): String {
        val builder = StringBuilder()
        builder.append("/")
        builder.append(cityName)
        builder.append("/")
        builder.append(orderId)
        return builder.toString()
    }


    interface RealtimeTrackingInterface {
        fun getUpdatedLocation(trackingDataModel: TrackingDataModel?)
    }
}