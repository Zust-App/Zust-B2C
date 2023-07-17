package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.orderDetail.models.Address
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    fun isProfileCreated(): Boolean {
        return sharedPrefManager.checkIsProfileCreate()
    }

    fun isAuthTokenFound(): Boolean {
        return !sharedPrefManager.getUserAuthToken().isNullOrEmpty()
    }
    fun getSavedAddressFound():Boolean{
        sharedPrefManager.getUserAddress()?.pincode?.let {
             return true
        }?:run {
            return false
        }
    }


}