package com.aravindcraj.px.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aravindcraj.px.data.db.dao.PhotosDao
import com.aravindcraj.px.data.models.Photo

@Database(
    entities = [Photo::class],
    version = 1,
    exportSchema = false
)
abstract class PxDatabase : RoomDatabase() {
    abstract fun photosDao(): PhotosDao

    companion object {
        @Volatile
        private var INSTANCE: PxDatabase? = null

        fun getInstance(context: Context): PxDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PxDatabase::class.java, "px.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}