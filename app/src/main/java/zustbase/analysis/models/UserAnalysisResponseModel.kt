package zustbase.analysis.models


data class UserAnalysisResponseModel(val data: UserAnalysisResponseData? = null)

data class UserAnalysisResponseData(
    val userReport: UserReport? = null,
    val topGainer: List<TopGainer>? = null,
)

data class UserReport(
    val orderCount: Int? = null,
    val expense: Double? = null,
    val ranking: Int? = null,
    val zustPurchaseCredit: Int? = null,
    val maxPurchaseCredit: Int? = null,
)

data class TopGainer(val name: String? = null, val expense: Double? = null)