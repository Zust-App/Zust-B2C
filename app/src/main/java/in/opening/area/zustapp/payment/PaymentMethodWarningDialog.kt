package `in`.opening.area.zustapp.payment

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.databinding.PaymentMethodWarningUiBinding

class PaymentMethodWarningDialog {
    private var dialog: AlertDialog? = null

    fun showDialog(context: Context?, confirmCallback: () -> Unit, cancelCallback: () -> Unit) {
        if (context == null) {
            return
        }
        val binding: PaymentMethodWarningUiBinding = PaymentMethodWarningUiBinding.inflate(LayoutInflater.from(context))
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context).setView(binding.root)
        if (dialog == null) {
            dialog = materialAlertDialogBuilder.show()
        }
        if (dialog?.isShowing == false) {
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.show()
        }
        binding.confirmButton.setOnClickListener {
            confirmCallback.invoke()
            closeDialog()
        }
        binding.cancelButton.setOnClickListener {
            cancelCallback.invoke()
            closeDialog()
        }
        // binding.subTitleText.text = ""
    }

    private fun closeDialog() {
        dialog?.cancel()
        dialog?.hide()
    }
}