package zustbase.services.uiModel

import zustbase.basepage.models.ZustServicePageResponse
import zustbase.services.models.ZustServiceData

sealed interface ZustAvailServicesUiModel {
    val isLoading: Boolean

    data class Success(val data: ZustServiceData? = null, val zustServicePageResponse: ZustServicePageResponse? = null, override val isLoading: Boolean) : ZustAvailServicesUiModel
    data class Empty(override val isLoading: Boolean) : ZustAvailServicesUiModel

    data class ErrorUi(override val isLoading: Boolean, var message: String?, val errorCode: Int? = null) : ZustAvailServicesUiModel
}