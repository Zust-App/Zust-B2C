package zustbase.analysis.uimodels

import zustbase.analysis.models.UserAnalysisResponseData

sealed interface UserReportAnalysisUiModel{
    val isLoading:Boolean

    data class Success(override val isLoading: Boolean,val data: UserAnalysisResponseData):UserReportAnalysisUiModel
    data class Error(override val isLoading: Boolean,val errorMessage:String?=null):UserReportAnalysisUiModel
    data class Empty(override val isLoading: Boolean):UserReportAnalysisUiModel

}