package `in`.opening.area.zustapp.storage.datastore

import zustbase.orderDetail.models.Address
import `in`.opening.area.zustapp.profile.models.ProfileData
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor() {
    @Inject
    internal lateinit var dataStoreWrapper: DataStoreWrapper

    @Inject
    lateinit var gson: Gson


    suspend fun saveUserProfileDetails(profileData: ProfileData?) {
        try {
            if (profileData == null) {
                return
            }
            val string = gson.toJson(profileData, ProfileData::class.java)
            dataStoreWrapper.saveUserProfile(string)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getSavedUserProfile(): Flow<ProfileData?> {
        return dataStoreWrapper.getUserProfile().map {
            gson.fromJson(it, ProfileData::class.java)
        }
    }


}