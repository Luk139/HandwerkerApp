package com.example.hingesensornew.distance.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DistanceItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Delete
    suspend fun deleteDistanceItem(distanceItemEntity: DistanceItemEntity)

    @Query("SELECT * FROM DistanceItemEntity")
    fun getAllDistanceItems(): Flow<List<DistanceItemEntity>>

    @Query("SELECT * FROM DistanceItemEntity WHERE measurementOwnerId = :id")
    fun getAllDistanceItemsByMeasurementId(id:Int): Flow<List<DistanceItemEntity>>

    @Update
    suspend fun updateDistanceItem(distanceItemEntity: DistanceItemEntity)
}