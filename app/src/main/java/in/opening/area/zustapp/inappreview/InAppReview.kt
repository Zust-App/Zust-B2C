package `in`.opening.area.zustapp.inappreview

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
                flow.addOnCompleteListener { _ ->

                }.addOnFailureListener {ex->
                    Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}