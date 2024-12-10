package com.example.hingesensornew.hingesensor.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HingeSensorItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Update
    suspend fun updateHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Delete
    suspend fun deleteHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Query("SELECT * FROM HingeSensorItemEntity")
    fun getAllHingeSensorItem(): Flow<List<HingeSensorItemEntity>>

    @Query("SELECT * FROM HingeSensorItemEntity WHERE measurementOwnerId = :id")
    fun getAllHingeSensorItemByMeasurementId(id:Int): Flow<List<HingeSensorItemEntity>>
}