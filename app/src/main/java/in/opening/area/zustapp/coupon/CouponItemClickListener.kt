package `in`.opening.area.zustapp.coupon

import `in`.opening.area.zustapp.coupon.model.Coupon

interface CouponItemClickListener {
    fun didTapOnApply(item: Coupon)
}