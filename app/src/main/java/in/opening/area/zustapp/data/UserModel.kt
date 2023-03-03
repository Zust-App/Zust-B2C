package `in`.opening.area.zustapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "user")
data class UserModel(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    var id: Int? = Random.nextInt())