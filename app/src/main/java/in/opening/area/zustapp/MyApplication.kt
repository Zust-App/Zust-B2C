package `in`.opening.area.zustapp

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
    }

    override fun onTerminate() {
        super.onTerminate()
    }


    companion object {
        lateinit var appContext: Context

        fun getApplication(): Context {
            return appContext
        }
    }
}