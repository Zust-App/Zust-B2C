package subscription.model

import `in`.opening.area.zustapp.utility.UserCustomError

class SubscriptionFormResponseModel(val data:Boolean, val errors: List<UserCustomError>? = null,
                                    val message: String? = null,
                                    val statusCode: Int? = -1,)