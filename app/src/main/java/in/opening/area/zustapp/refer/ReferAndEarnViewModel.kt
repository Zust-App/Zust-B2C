package `in`.opening.area.zustapp.refer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.profile.models.Refer
import `in`.opening.area.zustapp.refer.data.UserReferralDetail
import `in`.opening.area.zustapp.refer.uistate.UserReferralDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReferAndEarnViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    internal var referCache: Refer? = null

    private val _userReferralDetailUiState = MutableStateFlow<UserReferralDetailUiState>(UserReferralDetailUiState.Initial(false))
    internal val userReferralDetailUiState: StateFlow<UserReferralDetailUiState> get() = _userReferralDetailUiState

    internal fun getUserReferralDetails() = viewModelScope.launch {
        _userReferralDetailUiState.update {
            UserReferralDetailUiState.Initial(true)
        }
        when (val response = apiRequestManager.getUserReferralDetails()) {
            is ResultWrapper.Success -> {
                val data = response.value.data
                data?.userReferralDetails?.let { referralData ->
                    val referralLevelMap: Map<Int, List<UserReferralDetail>> = referralData.groupBy { it.level }
                    _userReferralDetailUiState.update {
                        UserReferralDetailUiState.Success(referralLevelMap, data.totalReferralIncome, data.message, false)
                    }
                } ?: run {
                    _userReferralDetailUiState.update {
                        UserReferralDetailUiState.Error(false, response.value.error)
                    }
                }
            }

            is ResultWrapper.NetworkError -> {
                _userReferralDetailUiState.update {
                    UserReferralDetailUiState.Error(false, "Something went wrong. Please try again later")
                }
            }

            is ResultWrapper.GenericError -> {
                _userReferralDetailUiState.update {
                    UserReferralDetailUiState.Error(false, response.error?.error ?: "Something went wrong. Please try again later")
                }
            }

            else -> {
                _userReferralDetailUiState.update {
                    UserReferralDetailUiState.Error(false, "Something went wrong. Please try again later")
                }
            }
        }
    }




}