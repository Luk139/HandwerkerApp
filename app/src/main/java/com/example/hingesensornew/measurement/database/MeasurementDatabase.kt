package com.example.hingesensornew.measurement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hingesensornew.distance.database.DistanceItemEntity
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [MeasurementEntity::class,DistanceItemEntity::class,HingeSensorItemEntity::class],
    version = 1
)
abstract class MeasurementDatabase: RoomDatabase(){

    abstract fun measurementDao(): MeasurementDao

    companion object {
        @Volatile
        private var INSTANCE: MeasurementDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): MeasurementDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeasurementDatabase::class.java,
                    "measurement_database"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
