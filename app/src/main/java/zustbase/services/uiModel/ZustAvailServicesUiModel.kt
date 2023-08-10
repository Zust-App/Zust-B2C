package zustbase.services.uiModel

import zustbase.services.models.ZustServiceData

sealed interface ZustAvailServicesUiModel {
    val isLoading:Boolean
    data class Success(val data: ZustServiceData? = null, override val isLoading: Boolean):ZustAvailServicesUiModel
    data class Empty(override val isLoading: Boolean):ZustAvailServicesUiModel
}