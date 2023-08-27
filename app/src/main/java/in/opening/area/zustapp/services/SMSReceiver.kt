package `in`.opening.area.zustapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSReceiver() : BroadcastReceiver(), Parcelable {
    private var otpListener: OTPReceiveListener? = null

    constructor(parcel: Parcel) : this()

    fun setOTPListener(otpListener: OTPReceiveListener?) {
        this.otpListener = otpListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.extras != null) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                parseOtpStatus(extras)
            }
        }
    }

    private fun parseOtpStatus(extras: Bundle?) {
        if (extras != null) {
            val status = extras[SmsRetriever.EXTRA_STATUS] as Status?
            if (status != null) {
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        message?.let { extractOTP(it) }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        //no need of this
                    }
                }
            }
        }
    }

    private fun extractOTP(msg: String) {

        val message = msg.lowercase()
        if (message.contains("account", ignoreCase = true) || message.contains("zust", ignoreCase = true) ||
            message.contains("grinzy", ignoreCase = true) || message.contains("zust app", ignoreCase = true) ||
            message.contains("zustapp", ignoreCase = true)
        ) {
            val otpMatcher = Pattern.compile("\\b\\d{4}\\b").matcher(message)
            if (otpMatcher.find()) {
                val otp = otpMatcher.group(0)
                if (otp != null && otp.length == 4) {
                    otpListener?.onOTPReceived(otp)
                }else{
                   //
                }
            }else{
                //
            }
        } else {
            //
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun otpAutoError(error: String)
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