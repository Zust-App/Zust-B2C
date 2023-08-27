package subscription.model

data class SubscriptionFormReqBody(
    val inputDailyItems: String,
    val thirdDayNeedItems: String,
    val weeklyNeedItems: String,
    val anythingElse: String,
    val pinCode:String
)