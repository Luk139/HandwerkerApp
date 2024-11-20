package com.example.hingesensornew.distance.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

interface DistanceItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Delete
    suspend fun deleteDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Query("SELECT * FROM DistanceItemEntity")
    suspend fun getAllDistanceItems(): List<DistanceItemEntity>

    @Update
    suspend fun updateDistanceItem(distanceItemEntity: DistanceItemEntity)
}