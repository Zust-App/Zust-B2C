package subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import subscription.model.SubscriptionFormReqBody
import subscription.uiModel.SubscriptionFormUiModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(private val apiRequestManager: ApiRequestManager) : ViewModel() {
    private val _subscriptionFormUiModel = MutableStateFlow<SubscriptionFormUiModel>(SubscriptionFormUiModel.Empty(false))
    val subscriptionFormUiModel: StateFlow<SubscriptionFormUiModel> get() = _subscriptionFormUiModel

    internal fun submitSubscriptionRequest(inputDailyItems: String, thirdDayNeedItems: String, weeklyNeedItems: String, anythingElse: String, pincode: String) = viewModelScope.launch(Dispatchers.IO) {
        val subscriptionFormReqBody = SubscriptionFormReqBody(inputDailyItems, thirdDayNeedItems, weeklyNeedItems, anythingElse, pincode)
        _subscriptionFormUiModel.update { SubscriptionFormUiModel.Empty(true) }
        when (val response = apiRequestManager.sendSubscriptionFormResponse(subscriptionFormReqBody)) {
            is ResultWrapper.Success -> {
                if (response.value.data) {
                    _subscriptionFormUiModel.update { SubscriptionFormUiModel.Success(false, data = response.value.data) }
                } else {
                    _subscriptionFormUiModel.update { SubscriptionFormUiModel.Error(false, response.value.message) }
                }
            }

            is ResultWrapper.NetworkError -> {
                _subscriptionFormUiModel.update { SubscriptionFormUiModel.Error(false, "Something went wrong") }
            }

            is ResultWrapper.UserTokenNotFound -> {
                _subscriptionFormUiModel.update { SubscriptionFormUiModel.Error(false, "Something went wrong") }
            }

            is ResultWrapper.GenericError -> {
                _subscriptionFormUiModel.update { SubscriptionFormUiModel.Error(false, "Something went wrong") }

            }
        }
    }
    internal fun resetFeedbackFormUiModel(){
        _subscriptionFormUiModel.update { SubscriptionFormUiModel.Empty(false) }
    }

}