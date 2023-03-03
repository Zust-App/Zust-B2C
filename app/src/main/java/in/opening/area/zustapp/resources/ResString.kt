package `in`.opening.area.zustapp.resources

import `in`.opening.area.zustapp.R
import android.content.Context
import androidx.collection.LruCache
import `in`.opening.area.zustapp.MyApplication
import javax.inject.Singleton

@Singleton
open class ResString {
    private var lruCache: LruCache<String, String> = LruCache(10)

    private fun loadString() {
        lruCache.evictAll()
    }

    open fun getStringKey(key: String): String {
        return lruCache.get(key) ?: getKeyFromAndroidRes(key)
    }

    private fun getKeyFromAndroidRes(key: String): String {
        val context:Context = MyApplication.getApplication()
        return context.resources.getString(getResIdUsingString(key))
    }

    private fun getResIdUsingString(key: String): Int {
        return when (key) {
            "Name" -> {
                R.string.app_name
            }
            else->{
                R.string.app_name
            }
        }
    }
}
