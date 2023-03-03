package `in`.opening.area.zustapp.session

import `in`.opening.area.zustapp.storage.datastore.DataStoreManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor() : AuthCallback {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

}

interface AuthCallback {
    fun didReceiveToken(authToken: String?) {

    }
}

