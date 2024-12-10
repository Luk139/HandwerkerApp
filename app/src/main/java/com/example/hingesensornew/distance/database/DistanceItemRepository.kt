package com.example.hingesensornew.distance.database

import com.example.hingesensornew.measurement.database.MeasurementDao
import kotlinx.coroutines.flow.Flow

class DistanceItemRepository (
    private val dao: MeasurementDao
){
    suspend fun insertDistanceItem(distanceItemEntity: DistanceItemEntity){
        dao.insertDistanceItem(distanceItemEntity)
    }

    suspend fun updateDistanceItem(distanceItemEntity: DistanceItemEntity){
        dao.updateDistanceItem(distanceItemEntity)
    }

    suspend fun deleteDistanceItem(distanceItemEntity: DistanceItemEntity){
        dao.deleteDistanceItem(distanceItemEntity)
    }
}