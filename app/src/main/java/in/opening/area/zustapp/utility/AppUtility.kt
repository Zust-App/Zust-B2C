package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.MyApplication
import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.offline.OfflineActivity
import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.text.Html
import android.widget.Toast
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import javax.inject.Inject


class AppUtility @Inject constructor() {
    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    companion object {
        const val WA_PACKAGE_NAME = "com.whatsapp"
        const val BUSINESS_WA_PACKAGE_NAME = "com.whatsapp.w4b"
        private val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=" + getPackageName()
        const val WEB_URL = "www.zustapp.com"
        private fun getPackageName(): String {
            return MyApplication.getApplication().packageName ?: "in.opening.area.zustapp"
        }

        fun getSharableTextOfReferralCode(referralCode: String): String {
            val referralText = "Hey please use my Referral Code <b>$referralCode</b> to download <b>Zust</b> app and get upto 60% off on your Order\n Download app $PLAY_STORE_URL"
            return Html.fromHtml(referralText).toString()
        }

        @JvmStatic
        fun checkIfOnline(context: Context): Boolean {
            val cm = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
            val capabilities = cm?.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }

        @JvmStatic
        fun showToast(context: Context?, msg: String?) {
            if (context == null) {
                return
            }
            if (msg.isNullOrEmpty()) {
                return
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }


        fun showShareIntent(context: Context?, shareText: String?) {
            if (context == null) {
                return
            }
            if (shareText == null) {
                return
            }
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            sendIntent.type = "text/plain"
            context.startActivity(Intent.createChooser(sendIntent, "Share " + context.getString(R.string.app_name)))
        }

        fun copyToClipboard(context: Context?, text: String?, label: String) {
            if (context == null) {
                return
            }
            if (text == null) {
                return
            }
            val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
            val clip = ClipData.newPlainText(label, text)
            clipboard?.setPrimaryClip(clip)
            showToast(context, context.getString(R.string.copied))
        }

        fun getAuthError(): UserCustomError {
            return UserCustomError("Error", "Token Expired")
        }

        fun getAuthErrorArrayList(): ArrayList<UserCustomError> {
            return arrayListOf(UserCustomError("Error", "Token Expired"))
        }

        fun getNotDeliverableErrorArrayList(): ArrayList<UserCustomError> {
            return arrayListOf(UserCustomError("Error", "We are not delivering here"))
        }

        fun isAppInstalled(packageName: String): Boolean {
            val pm: PackageManager = MyApplication.getApplication().packageManager
            return try {
                pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        fun getWhatsappHelpUrl(): String {
            return "https://api.whatsapp.com/send?phone=" + "7858906229"
        }

        fun showNoInternetActivity(context: Context?) {
            if (context == null) {
                return
            }
            val intent = Intent(context, OfflineActivity::class.java)
            context.startActivity(intent)
        }

        fun openPlayStore(context: Context?) {
            if (context == null) {
                return
            }
            val appPackageName = getPackageName() // replace with your app package name
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // Play Store app is not installed, open the link in a browser
                intent.data = Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                context.startActivity(intent)
            }
        }

        fun showAppUpdateDialog(context: Context?, canExecutePlayStore: Boolean, positiveCallback: () -> Unit) {
            if (context == null) {
                return
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("App update")
            builder.setMessage("A new app update is available. Please use for better experience")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                if (canExecutePlayStore) {
                    openPlayStore(context)
                } else {
                    positiveCallback.invoke()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun openEmailIntent(context: Context?, emailId: String) {
            try {
                if (context == null) {
                    return
                }
                val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", emailId, null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text")
                context.startActivity(Intent.createChooser(emailIntent, "Email to us"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun openCallIntent(context: Context?, phoneNumber: String) {
            try {
                if (context == null) {
                    return
                }
                val phone = if (phoneNumber.contains("+91")) {
                    phoneNumber
                } else {
                    "+91$phoneNumber"
                }
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
