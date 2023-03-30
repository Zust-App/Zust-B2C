package `in`.opening.area.zustapp.refer

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.profile.models.Refer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReferAndEarnViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal var referCache: Refer? = null
}