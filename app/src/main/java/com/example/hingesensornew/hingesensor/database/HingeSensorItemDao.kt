package com.example.hingesensornew.hingesensor.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


interface HingeSensorItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Update
    suspend fun updateHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Delete
    suspend fun deleteHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity)

    @Query("SELECT * FROM HingeSensorItemEntity")
    suspend fun getAllHingeSensorItem(): List<HingeSensorItemEntity>
}