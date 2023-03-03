package `in`.opening.area.zustapp.helper

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class InAppUpdateManager(private val activity: Activity, private val callback: () -> Unit) : InstallStateUpdatedListener {
    companion object {
        private const val MY_REQUEST_CODE = 500
    }

    private var appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private var currentType = AppUpdateType.FLEXIBLE

    init {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (info.updatePriority() == 5) {
                    if (info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startUpdate(info, AppUpdateType.IMMEDIATE)
                    }
                }
                else if (info.updatePriority() == 4) {
                    val clientVersionStalenessDays = info.clientVersionStalenessDays()
                    if (clientVersionStalenessDays != null && clientVersionStalenessDays >= 5 && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startUpdate(info, AppUpdateType.IMMEDIATE)
                    } else if (clientVersionStalenessDays != null && clientVersionStalenessDays >= 3 && info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        startUpdate(info, AppUpdateType.FLEXIBLE)
                    }
                }
                else if (info.updatePriority() == 3) { // Priority: 3
                    val clientVersionStalenessDays = info.clientVersionStalenessDays()
                    if (clientVersionStalenessDays != null && clientVersionStalenessDays >= 30 && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startUpdate(info, AppUpdateType.IMMEDIATE)
                    } else if (clientVersionStalenessDays != null && clientVersionStalenessDays >= 15 && info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        startUpdate(info, AppUpdateType.FLEXIBLE)
                    }
                }
                else if (info.updatePriority() == 2) { // Priority: 2
                    val clientVersionStalenessDays = info.clientVersionStalenessDays()
                    if (clientVersionStalenessDays != null && clientVersionStalenessDays >= 90 && info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        startUpdate(info, AppUpdateType.IMMEDIATE)
                    } else if (clientVersionStalenessDays != null && clientVersionStalenessDays >= 30 && info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        startUpdate(info, AppUpdateType.FLEXIBLE)
                    }
                }
                else if (info.updatePriority() == 1) {
                    startUpdate(info, AppUpdateType.FLEXIBLE)
                }
            }
        }
        appUpdateManager.registerListener(this)
    }


    private fun startUpdate(info: AppUpdateInfo, type: Int) {
        appUpdateManager.startUpdateFlowForResult(info, type, activity, MY_REQUEST_CODE)
        currentType = type
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (currentType == AppUpdateType.FLEXIBLE) {
                if (info.installStatus() == InstallStatus.DOWNLOADED)
                    flexibleUpdateDownloadCompleted()
            } else if (currentType == AppUpdateType.IMMEDIATE) {
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    startUpdate(info, AppUpdateType.IMMEDIATE)
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != AppCompatActivity.RESULT_OK) {
                Log.e("ERROR", "Update flow failed! Result code: $resultCode")
            }
        }
    }

    private fun flexibleUpdateDownloadCompleted() {
        callback.invoke()
    }
    fun proceedAppUpdate(){
        appUpdateManager.completeUpdate()
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(this)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            flexibleUpdateDownloadCompleted()
        }
    }

}