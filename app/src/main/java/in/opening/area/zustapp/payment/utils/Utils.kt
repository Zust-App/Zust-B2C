package `in`.opening.area.zustapp.payment.utils

import android.util.Base64
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.payment.models.TimeSlot
import androidx.recyclerview.widget.DiffUtil
import com.phonepe.intent.sdk.api.B2BPGRequest
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest

val paymentMethodDiff = object : DiffUtil.ItemCallback<PaymentMethod>() {
    override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
        return oldItem == newItem
    }
}

val timeSlotDiff = object : DiffUtil.ItemCallback<TimeSlot>() {
    override fun areItemsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
        return oldItem.timeSlot == newItem.timeSlot
    }

    override fun areContentsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
        return oldItem == newItem
    }

}

internal fun sha256(input: String): String {
    val bytes = input.toByteArray(Charset.defaultCharset())
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%20x".format(it) }
}

internal fun createPaymentRequestEncoded(
    txnId: String,
    merchantId: String,
    mobileNum: String,
    callbackUrl: String,
    intentType: String,
    targetApp: String,
): String {

    val data = JSONObject()
    data.put("merchantTransactionId", txnId)
    data.put("merchantId", merchantId)
    data.put("amount", 200)
    data.put("mobileNumber", mobileNum)
    data.put("callbackUrl", callbackUrl)
    val paymentInstrument = JSONObject()
    paymentInstrument.put("type", intentType)
    paymentInstrument.put("targetApp", targetApp)
    data.put("paymentInstrument", paymentInstrument)
    val deviceContext = JSONObject()
    deviceContext.put("deviceOS", "ANDROID")
    data.put("deviceContext", deviceContext)
    return Base64.encodeToString(
        data.toString().toByteArray(Charset.defaultCharset()), Base64.NO_WRAP
    )
}
fun createB2BPaymentReq(base64Body:String,checksum:String): B2BPGRequest {
    return  B2BPGRequestBuilder()
        .setData(base64Body)
        .setChecksum(checksum)
        .setUrl(PG_ApiEndPoint)
        .build()
}
const val PG_ApiEndPoint = "/pg/v1/pay"
const val PG_saltIndex = "1"
const val PG_saltKey = "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399"

