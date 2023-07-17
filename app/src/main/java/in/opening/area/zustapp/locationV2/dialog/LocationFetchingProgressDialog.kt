package `in`.opening.area.zustapp.locationV2.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import `in`.opening.area.zustapp.R

class LocationFetchingProgressDialog {
    private var dialog: AlertDialog? = null
    fun showDialog(context: Context?) {
        if (context == null) {
            return
        }
        val view = LayoutInflater.from(context).inflate(R.layout.custom_alert_ui, null)
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context).setView(view)
        if (dialog==null){
            dialog = materialAlertDialogBuilder.show()
        }
        if (dialog?.isShowing == false) {
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.show()
        }
    }

    fun closeDialog() {
        dialog?.cancel()
        dialog?.hide()
    }
}
