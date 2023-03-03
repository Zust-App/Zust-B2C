package `in`.opening.area.zustapp.di

import `in`.opening.area.zustapp.storage.datastore.SharedPrefManager
import android.content.Context
import androidx.room.Room
import `in`.opening.area.zustapp.storage.db.GreenBoyzDatabase
import `in`.opening.area.zustapp.storage.db.GreenBoyzDatabase.Companion.DB_NAME
import `in`.opening.area.zustapp.storage.db.UserDao
import `in`.opening.area.zustapp.storage.db.dao.AddToCartDao
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideBlogDatabase(
        @ApplicationContext context: Context
    ): GreenBoyzDatabase =
        Room.databaseBuilder(context, GreenBoyzDatabase::class.java, DB_NAME).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUserDao(database: GreenBoyzDatabase): UserDao = database.userDao()

    @Provides
    @Singleton
    fun provideAddToCartDao(database: GreenBoyzDatabase): AddToCartDao = database.addToCartDao()


    @Singleton
    @Provides
    fun provideSharedPrefManager(context: Context): SharedPreferences {
        return context.getSharedPreferences(SharedPrefManager.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}