package `in`.opening.area.zustapp.storage.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import `in`.opening.area.zustapp.data.UserModel
import `in`.opening.area.zustapp.product.model.ProductSingleItem
import `in`.opening.area.zustapp.storage.db.dao.AddToCartDao
import non_veg.storage.NonVegItemLocalModel
import non_veg.storage.dao.NonVegAddToCartDao


@Database(
    exportSchema = true,
    version = 3,
    entities = [
        UserModel::class,
        ProductSingleItem::class,
        NonVegItemLocalModel::class
    ],
    autoMigrations = [AutoMigration(from = 2, to = 3)]
)

abstract class ZustAppDatabase : RoomDatabase() {
    companion object {
        const val DB_NAME = "zust_app_db"
    }

    abstract fun userDao(): UserDao
    abstract fun addToCartDao(): AddToCartDao

    abstract fun nonVegAddToCartDao(): NonVegAddToCartDao

}