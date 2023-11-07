package `in`.opening.area.zustapp.payment.utils

import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Base64
import `in`.opening.area.zustapp.payment.models.PaymentMethod
import `in`.opening.area.zustapp.payment.models.TimeSlot
import androidx.recyclerview.widget.DiffUtil
import com.phonepe.intent.sdk.api.B2BPGRequest
import com.phonepe.intent.sdk.api.B2BPGRequestBuilder
import com.phonepe.intent.sdk.api.PhonePe
import com.phonepe.intent.sdk.api.PhonePe.getPackageSignature
import `in`.opening.area.zustapp.BuildConfig
import org.json.JSONObject
import java.nio.charset.Charset
import java.security.MessageDigest

const val isPgTestEnv = false
const val PG_ApiEndPoint = "/pg/v1/pay"
const val PG_saltIndex = "1"

val PG_saltKey = if (isPgTestEnv) {
    "099eb0cd-02cf-4e2a-8aca-3e6c6aff0399"
} else {
    "e2a24a0e-bd26-4ab6-b529-ab25eda6bfb5"
}
val pg_merchant_key = if (isPgTestEnv) {
    "PGTESTPAYUAT"
} else {
    "M1O8N18KU2RP"
}
const val simulatorPackage = "com.phonepe.simulator"
val appId: String = if (isPgTestEnv) {
    if (BuildConfig.DEBUG) {
        getPackageSignature()
    } else {
        getPackageSignature()
    }
} else {
    getPackageSignature()
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
    amount: Double,
): String {

    val data = JSONObject()
    data.put("merchantTransactionId", txnId)
    data.put("merchantId", merchantId)
    data.put("amount", amount)
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

fun createB2BPaymentReq(base64Body: String, checksum: String): B2BPGRequest {
    return B2BPGRequestBuilder()
        .setData(base64Body)
        .setChecksum(checksum)
        .setUrl(PG_ApiEndPoint)
        .build()
}

