package `in`.opening.area.zustapp.webpage

import `in`.opening.area.zustapp.utility.AppUtility
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient


@SuppressLint("SetJavaScriptEnabled")
fun WebView?.webViewExtension(webUrl: String, context: Context, progress: (Int) -> Unit, showHidePgBar: (Boolean) -> Unit) {
    this?.loadUrl(webUrl)
    this?.settings?.javaScriptEnabled = true
    this?.settings?.domStorageEnabled = true
    this?.webViewClient = object : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                view?.loadUrl(url)
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showHidePgBar.invoke(true)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            showHidePgBar.invoke(false)
        }

        @Deprecated("Deprecated in Java", ReplaceWith("showToast(this@InAppWebActivity, \"Unable to load, something went wrong\")", "in.opening.area.greenboyz.codes.utility.AppUtility.Companion.showToast"))
        override fun onReceivedError(
            view: WebView?,
            errorCode: Int,
            description: String?,
            failingUrl: String?,
        ) {
            showHidePgBar.invoke(false)
            progress.invoke(100)
            AppUtility.showToast(context, "Unable to load, something went wrong")
        }

    }

    this?.webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progress.invoke(newProgress)
        }
    }
}

