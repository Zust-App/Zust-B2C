package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ApiRequestManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal val directionsResponse = MutableLiveData<String>()

    internal val wayPointsResponse = MutableLiveData<String>()
    fun getDirections(origin: LatLng, destination: LatLng) = viewModelScope.launch {
        val value = apiRequestManager.getDirections(origin, destination)
        directionsResponse.postValue(value)
    }

    fun getDirectionWithWayPoints(origin: LatLng, destination: LatLng, between: LatLng) = viewModelScope.launch {
        val value = apiRequestManager.getDirectionsWithWayPoints(origin, destination, between)
        wayPointsResponse.postValue(value)
    }
}