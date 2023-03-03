package `in`.opening.area.zustapp.storage

import android.util.LruCache
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class Cache @Inject constructor() {
    private val lru: LruCache<String, Any> by lazy { LruCache(1024*5) }

    fun saveData(key: String, data: Any?) {
        if (data == null) {
            return
        }
        lru.put(key, data)
    }

    fun <T : Any?> getData(key: String): T? {
        val data = lru[key] ?: return null
        return data as T
    }

    fun removeKey(key: String) {
        lru.remove(key)
    }
}