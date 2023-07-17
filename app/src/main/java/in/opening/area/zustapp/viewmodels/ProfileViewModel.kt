package `in`.opening.area.zustapp.viewmodels

import `in`.opening.area.zustapp.network.ApiRequestManager
import `in`.opening.area.zustapp.network.ResultWrapper
import `in`.opening.area.zustapp.profile.models.CustomerSupport
import `in`.opening.area.zustapp.profile.models.Refer
import `in`.opening.area.zustapp.profile.models.SuggestProductReqModel
import `in`.opening.area.zustapp.repository.ProductRepo
import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import `in`.opening.area.zustapp.uiModels.UserProfileUi
import `in`.opening.area.zustapp.utility.AppUtility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiRequestManager: ApiRequestManager, private val productRepo: ProductRepo,
) : ViewModel() {

    internal val profileUiState = MutableStateFlow<UserProfileUi>(UserProfileUi.InitialUi(false))


    internal val suggestProductResponse = MutableStateFlow(JSONObject())

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    internal val moveToLoginPage = MutableStateFlow(false)

    internal fun getUserProfileResponse() = viewModelScope.launch {
        profileUiState.update { UserProfileUi.InitialUi(true) }
        when (val response = apiRequestManager.getUserProfileDetails()) {
            is ResultWrapper.Success -> {
                if (response.value.data != null) {
                    profileUiState.update { (UserProfileUi.ProfileSuccess(false, response.value.data)) }
                    dataStoreManager.saveUserProfileDetails(response.value.data)
                } else {
                    if (response.value.statusCode == 401) {
                        moveToLoginPage.update { true }
                        return@launch
                    } else {
                        profileUiState.update { UserProfileUi.ErrorUi(false, errors = response.value.errors ?: arrayListOf()) }
                    }
                }
            }

            is ResultWrapper.GenericError -> {
                if (response.code == 401) {
                    moveToLoginPage.update { true }
                    return@launch
                } else {
                    profileUiState.update { UserProfileUi.ErrorUi(false, errorMsg = response.error?.error ?: "Something went wrong") }
                }
            }

            is ResultWrapper.UserTokenNotFound -> {
                dataStoreManager.getSavedUserProfile().collect { profile ->
                    if (profile != null) {
                        profileUiState.update { (UserProfileUi.ProfileSuccess(false, profile)) }
                    } else {
                        profileUiState.update { UserProfileUi.ErrorUi(false, errors = AppUtility.getAuthErrorArrayList()) }
                    }
                }
            }

            is ResultWrapper.NetworkError -> {
                dataStoreManager.getSavedUserProfile().collect { profile ->
                    if (profile != null) {
                        profileUiState.update { (UserProfileUi.ProfileSuccess(false, profile)) }
                    } else {
                        profileUiState.update { UserProfileUi.ErrorUi(false, errorMsg = "Something went wrong") }
                    }
                }
            }
        }
    }

    internal fun sendUserSuggestProduct(suggestProductReqModel: SuggestProductReqModel) = viewModelScope.launch {
        val response = apiRequestManager.sendSuggestProductResponse(suggestProductReqModel)
        if (response is ResultWrapper.Success) {
            val jsonObject = JSONObject(response.value)
            if (jsonObject.has("data")) {
                val dataJSONObject = jsonObject.getJSONObject("data")
                suggestProductResponse.emit(dataJSONObject)
            }
        } else {
            suggestProductResponse.emit(JSONObject())
        }
    }


    internal fun getAboutUsUrl(): String? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.aboutUsUrl
        }
        return null
    }

    internal fun getOpenSourceUrl(): String? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.openSourceUrl
        }
        return null
    }

    internal fun playStoreUrl(): String? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.playStoreUrl
        }
        return null
    }

    internal fun faqUrl(): String? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.faqurl
        }
        return null
    }

    internal fun privacyPolicyUrl(): String? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.privacyPolicyUrl
        }
        return null
    }

    internal fun termAndConditionUrl(): String? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.termAndConditionUrl
        }
        return null
    }

    fun logoutUser() {
        sharedPrefManager.removeAuthToken()
        sharedPrefManager.removeSavedAddress()
        sharedPrefManager.removeMerchantId()
        viewModelScope.launch {
            productRepo.deleteAllProduct()
        }
    }

    internal fun getReferral(): Refer? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.refer
        }
        return null
    }

    fun getSupportCsDetail(): CustomerSupport? {
        if (profileUiState.value is UserProfileUi.ProfileSuccess) {
            return (profileUiState.value as UserProfileUi.ProfileSuccess).data?.customerSupport
        }
        return null
    }

    internal fun removeUserLocalData() = viewModelScope.launch {
        sharedPrefManager.apply {
            removeAuthToken()
            removeSavedAddress()
            removeIsProfileCreated()
            removePhoneNumber()
        }
    }

}