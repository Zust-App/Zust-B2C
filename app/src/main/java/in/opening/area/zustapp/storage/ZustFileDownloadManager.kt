package `in`.opening.area.zustapp.storage

import android.content.Context
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


private fun downloadHtml(url:String,context:Context) {
    val html: String = url // Get the current URL of the WebView
    val file = File(context.externalCacheDir, "html.html")
    try {
        val outputStream: OutputStream = FileOutputStream(file)
        outputStream.write(html.toByteArray())
        outputStream.close()
        Toast.makeText(context, "HTML downloaded to " + file.absolutePath, Toast.LENGTH_SHORT).show()
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error downloading HTML", Toast.LENGTH_SHORT).show()
    }
}