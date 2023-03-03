package `in`.opening.area.zustapp.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import `in`.opening.area.zustapp.data.UserModel
@Dao
interface UserDao {
    @Insert(onConflict = REPLACE)
    suspend fun setUser(userModel: UserModel)
}