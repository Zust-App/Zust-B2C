package `in`.opening.area.zustapp.utility

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.MyApplication
import `in`.opening.area.zustapp.offline.OfflineActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat


class AppUtility {

    companion object {
        const val WA_PACKAGE_NAME = "com.whatsapp"
        const val BUSINESS_WA_PACKAGE_NAME = "com.whatsapp.w4b"
        fun getSharableTextOfReferralCode(playStoreUrl: String, referralCode: String): String {
            return "Hey please use my Referral Code <b>$referralCode</b> to download ring app and get upto 60% off on your Order\n Download app $playStoreUrl"
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
            if (msg == null) {
                return
            }
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        @JvmStatic
        @Composable
        fun OfflineDialog(confirmButtonClick: () -> Unit) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text(text = stringResource(R.string.connection_error_title)) },
                text = { Text(text = stringResource(R.string.connection_error_message)) },
                confirmButton = {
                    TextButton(onClick = confirmButtonClick) {
                        Text(stringResource(R.string.retry_label))
                    }
                }
            )
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
            showToast(context, "Successfully Copied to clipboard")
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
            return "https://api.whatsapp.com/send?phone=" + "7564062907"
        }
        fun showNoInternetActivity(context: Context?) {
            if (context == null) {
                return
            }
            val intent = Intent(context, OfflineActivity::class.java)
            context.startActivity(intent)
        }
    }
}
