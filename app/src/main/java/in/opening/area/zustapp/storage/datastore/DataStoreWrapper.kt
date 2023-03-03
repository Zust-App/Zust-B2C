package `in`.opening.area.zustapp.storage.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreWrapper @Inject constructor(var context: Context) {

    private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

    private val prefUserLocation = stringPreferencesKey(PREF_USER_LOC)
    private val userProfile = stringPreferencesKey(USER_PROFILE)
    private val latestAddress = stringPreferencesKey(LATEST_ADDRESS)

    companion object {
        const val PREF_USER_LOC = "user_location"
        const val USER_PROFILE = "user_profile"
        const val LATEST_ADDRESS = "latest_address"
    }


    fun getUserLocation(): Flow<String?> = context.userPreferencesDataStore.data
        .map { preferences ->
            preferences[prefUserLocation]
        }

    suspend fun saveUserLocation(value: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[prefUserLocation] = value
        }
    }

    suspend fun getUserProfile(): Flow<String?> = context.userPreferencesDataStore.data
        .map { preferences ->
            preferences[userProfile]
        }


    suspend fun saveUserProfile(value: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[userProfile] = value
        }
    }


    suspend fun saveLatestAddress(value: String) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[latestAddress] = value
        }
    }

     fun getLatestSavedAddress(): Flow<String?> = context.userPreferencesDataStore.data
        .map { preferences ->
            preferences[latestAddress]
        }

    suspend fun removeKeyLatestAddress() {
        context.userPreferencesDataStore.edit { preferences ->
            preferences.remove(latestAddress)
        }
    }

}
