package `in`.opening.area.zustapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSReceiver() : BroadcastReceiver(), Parcelable {
    private var otpListener: OTPReceiveListener? = null

    constructor(parcel: Parcel) : this() {

    }

    fun setOTPListener(otpListener: OTPReceiveListener?) {
        this.otpListener = otpListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            if (status != null) {
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val sms = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        sms?.let {
                            if (sms.contains("zust", ignoreCase = true) || sms.contains("grinzy", ignoreCase = true)) {
                                val p = Pattern.compile("\\d+")
                                val m = p.matcher(it)
                                if (m.find()) {
                                    val otp = m.group()
                                    if (otpListener != null) {
                                        otpListener!!.onOTPReceived(otp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SMSReceiver> {
        override fun createFromParcel(parcel: Parcel): SMSReceiver {
            return SMSReceiver(parcel)
        }

        override fun newArray(size: Int): Array<SMSReceiver?> {
            return arrayOfNulls(size)
        }
    }
}