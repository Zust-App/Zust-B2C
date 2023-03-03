package `in`.opening.area.zustapp.utility

import android.content.Context
import android.widget.Toast

interface  ShowToast {
    fun showToast( context: Context?,msg: String?) {
        if (msg == null) {
            return
        }
        if (context==null){
            return
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}