package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val sharedPrefManager: SharedPrefManager) : ViewModel() {

    internal fun doesOnBoardingShown() = sharedPrefManager.doesOnBoardingShown()
    internal fun updateOnBoardingShown(isShown: Boolean) = sharedPrefManager.saveOnBoardingShown(isShown)

    fun isProfileCreated(): Boolean {
        return sharedPrefManager.checkIsProfileCreate()
    }

    fun isAuthTokenFound(): Boolean {
        return !sharedPrefManager.getUserAuthToken().isNullOrEmpty()
    }


}