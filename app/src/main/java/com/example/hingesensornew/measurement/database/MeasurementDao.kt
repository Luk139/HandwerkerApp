package com.example.hingesensornew.measurement.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.hingesensornew.distance.database.DistanceItemEntity
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurementEntity: MeasurementEntity)

    @Update
    suspend fun updateMeasurement(measurementEntity: MeasurementEntity)

    @Delete
    suspend fun deleteMeasurement(measurementEntity: MeasurementEntity)

    @Query("SELECT * FROM MeasurementEntity")
    fun getAllMeasurements():Flow<List<MeasurementEntity>>

    @Query("SELECT * FROM MeasurementEntity")
    fun getAllMeasurementsWithHingeSensorAndDistance():Flow<List<MeasurementAndDistanceItemAndHingeSensorItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Delete
    suspend fun deleteDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Update
    suspend fun updateDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Update
    suspend fun updateHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Delete
    suspend fun deleteHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

}