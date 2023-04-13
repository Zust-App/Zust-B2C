package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    fun isProfileCreated(): Boolean {
        return sharedPrefManager.checkIsProfileCreate()
    }

    fun isAuthTokenFound(): Boolean {
        return !sharedPrefManager.getUserAuthToken().isNullOrEmpty()
    }


}