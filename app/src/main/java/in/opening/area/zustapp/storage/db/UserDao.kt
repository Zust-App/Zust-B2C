package `in`.opening.area.zustapp.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import `in`.opening.area.zustapp.data.UserModel
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setUser(userModel: UserModel)
}