package `in`.opening.area.zustapp.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.opening.area.zustapp.data.UserModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.storage.db.dao.AddToCartDao

@Database(
    entities = [
        UserModel::class,
        ProductSingleItem::class
    ],
    version = 1
)
abstract class GreenBoyzDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "green_boyz_db"
    }

    abstract fun userDao(): UserDao
    abstract fun addToCartDao(): AddToCartDao

}