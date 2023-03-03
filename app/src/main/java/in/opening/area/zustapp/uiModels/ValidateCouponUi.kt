package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.coupon.model.AppliedCouponData
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface ValidateCouponUi {
    val isLoading: Boolean

    data class AppliedSuccessfully(override val isLoading: Boolean, val data: AppliedCouponData,
                                   val timeStamp: Long = System.currentTimeMillis(), val isCouponRemoved: Boolean = false) : ValidateCouponUi

    data class InitialUi(override val isLoading: Boolean) : ValidateCouponUi

    data class ErrorUi(override val isLoading: Boolean, val message: String? = "", val errors: List<UserCustomError> = arrayListOf()) : ValidateCouponUi
}