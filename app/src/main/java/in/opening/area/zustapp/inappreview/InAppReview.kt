package `in`.opening.area.zustapp.inappreview

import `in`.opening.area.zustapp.utility.AppUtility
import android.app.Activity
import android.widget.Toast
import com.google.android.play.core.review.ReviewManagerFactory

class InAppReview(private val context: Activity) {
    private val manager by lazy { ReviewManagerFactory.create(context) }
    private val request by lazy { manager.requestReviewFlow() }

    fun showInAppReviewDialog() {
        request.addOnCompleteListener { it ->
            if (it.isSuccessful) {
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(context, reviewInfo)
                flow.addOnCompleteListener { data ->
                    if (data.result==null && data.isComplete){
                        AppUtility.openPlayStore(context)
                    }else{
                        if (data.isSuccessful) {
                            Toast.makeText(context, "Thanks for rating Zust app", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { ex ->
                    AppUtility.openPlayStore(context)
                    Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            AppUtility.openPlayStore(context)
            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}