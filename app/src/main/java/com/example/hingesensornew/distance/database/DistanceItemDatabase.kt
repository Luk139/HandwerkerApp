package com.example.hingesensornew.distance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [DistanceItemEntity::class],
    version = 1
)
abstract class DistanceItemDatabase : RoomDatabase() {

    abstract fun distanceItemDao(): DistanceItemDao

    companion object {
        @Volatile
        private var INSTANCE: DistanceItemDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DistanceItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DistanceItemDatabase::class.java,
                    "distance_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}