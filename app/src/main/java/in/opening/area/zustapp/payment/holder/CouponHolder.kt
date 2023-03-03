package `in`.opening.area.zustapp.payment.holder

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.utility.ProductUtils
import `in`.opening.area.zustapp.databinding.ActivityPaymentBinding
import `in`.opening.area.zustapp.databinding.ApplyCouponContainerBinding
import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat

const val APPLY_COUPON = 1
const val REMOVE_COUPON = 2

class CouponHolder(binding: ActivityPaymentBinding?, private val callback: (Int) -> Unit) {
    private val couponBinding: ApplyCouponContainerBinding? = binding?.applyCouponContainer

    internal fun initiateViews() {
        couponBinding?.root?.setOnClickListener {
            callback.invoke(APPLY_COUPON)
        }
        couponBinding?.removeAppliedCoupon?.setOnClickListener {
            callback.invoke(REMOVE_COUPON)
        }
        setCouponData(null, null)
    }

    @SuppressLint("SetTextI18n")
    internal fun setCouponData(discountValue: Double?, couponName: String?) {
        if (discountValue == null || couponName == null) {
            couponBinding?.couponNextIcon?.visibility = View.VISIBLE
            couponBinding?.removeAppliedCoupon?.visibility = View.GONE
            couponBinding?.appliedCouponText?.visibility = View.GONE
            couponBinding?.appliedCouponText?.text = ""
            couponBinding?.couponApplyStatusIcon?.apply {
                setImageDrawable(ContextCompat.getDrawable(this.context,R.drawable.offer_icon))
            }
            couponBinding?.couponTitleTextView?.text = "Apply Coupon"
        } else {
            couponBinding?.couponNextIcon?.visibility = View.GONE
            couponBinding?.removeAppliedCoupon?.visibility = View.VISIBLE
            couponBinding?.appliedCouponText?.visibility = View.VISIBLE
            couponBinding?.couponTitleTextView?.text = "'$couponName' applied"
            couponBinding?.appliedCouponText?.text = "You saved ₹${ProductUtils.roundTo1DecimalPlaces(discountValue)} with this code"
            couponBinding?.couponApplyStatusIcon?.apply {
                setImageDrawable(ContextCompat.getDrawable(this.context,R.drawable.applied_coupon_icon))
            }
        }
    }
}