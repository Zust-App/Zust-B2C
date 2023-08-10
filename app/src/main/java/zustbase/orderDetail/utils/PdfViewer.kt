package zustbase.orderDetail.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

class PdfViewer {
    companion object {
        fun openPdfUsingOrderId(orderId: String?, context: Context?) {
            if (orderId == null) {
                return
            }
            val uri = Uri.parse("https:///services/irctc/ers?tm_booking_id=$orderId")
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            try {
                context?.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}