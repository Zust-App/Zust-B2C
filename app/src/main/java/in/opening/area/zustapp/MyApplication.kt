package `in`.opening.area.zustapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context

        fun getApplication(): Context {
            return appContext
        }
    }
}