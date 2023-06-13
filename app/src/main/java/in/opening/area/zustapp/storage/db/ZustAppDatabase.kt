package `in`.opening.area.zustapp.storage.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.opening.area.zustapp.data.UserModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.storage.db.dao.AddToCartDao


@Database(
    exportSchema = true,
    version = 2,
    entities = [
        UserModel::class,
        ProductSingleItem::class
    ],
    autoMigrations = [AutoMigration (from = 1, to = 2)]
)

abstract class ZustAppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "zust_app_db"
    }

    abstract fun userDao(): UserDao
    abstract fun addToCartDao(): AddToCartDao

}