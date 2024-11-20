package com.example.hingesensornew.hingesensor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [HingeSensorItemEntity::class],
    version = 1
)
abstract class HingeSensorItemDatabase : RoomDatabase() {

    abstract fun hingeSensorItemDao(): HingeSensorItemDao

    companion object {
        @Volatile
        private var INSTANCE: HingeSensorItemDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HingeSensorItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HingeSensorItemDatabase::class.java,
                    "hingeSensor_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
