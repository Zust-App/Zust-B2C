package `in`.opening.area.zustapp.address

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CustomMaterialDialog(private val callback:()->Unit) {
    companion object {
        const val TITLE = "Sorry"
        const val MESSAGE = "Sorry currently we are not deliver in your area."

    }

     fun showDialog(context: Context?) {
        if (context == null) {
            return
        }
        MaterialAlertDialogBuilder(context)
            .setTitle(TITLE)
            .setMessage(MESSAGE)
            .setPositiveButton("GOT IT") { _, _ ->
                callback.invoke()
            }
            .show()
    }
}