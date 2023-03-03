package `in`.opening.area.zustapp.uiModels

import `in`.opening.area.zustapp.coupon.model.Coupon
import `in`.opening.area.zustapp.utility.UserCustomError

sealed interface CouponListUi {
    val isLoading: Boolean

    data class CouponSuccess(
        override val isLoading: Boolean,
        val data: List<Coupon> = arrayListOf(),
        val timeStamp: Long = System.currentTimeMillis()
    ) : CouponListUi

    data class InitialUi(
        override val isLoading: Boolean,
        val timeStamp: Long = System.currentTimeMillis()
    ) : CouponListUi

    data class ErrorUi(
        override val isLoading: Boolean,
        val errors: List<UserCustomError> = arrayListOf(),
        val errorMsg: String? = "",
        val timeStamp: Long = System.currentTimeMillis()
    ) : CouponListUi
}