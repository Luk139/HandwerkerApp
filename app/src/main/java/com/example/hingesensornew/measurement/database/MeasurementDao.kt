package com.example.hingesensornew.measurement.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeasurement(measurementEntity: MeasurementEntity)

    @Update
    suspend fun updateMeasurement(measurementEntity: MeasurementEntity)

    @Transaction
    @Query("SELECT * FROM MeasurementEntity")
    suspend fun getAllMeasurementsWithHingeSensorItem(): List<MeasurementAndHingeSensorItem>

    @Delete
    suspend fun deleteMeasurement(measurementEntity: MeasurementEntity)
    
}