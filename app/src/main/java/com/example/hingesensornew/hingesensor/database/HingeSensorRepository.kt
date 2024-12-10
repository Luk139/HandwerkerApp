package com.example.hingesensornew.hingesensor.database

import com.example.hingesensornew.measurement.database.MeasurementDao
import kotlinx.coroutines.flow.Flow

class HingeSensorRepository(
    private val dao:MeasurementDao
) {
    suspend fun insertHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity){
        dao.insertHingeSensorItem(hingeSensorItemEntity)
    }

    suspend fun updateHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity){
        dao.updateHingeSensorItem(hingeSensorItemEntity)
    }

    suspend fun deleteHingeSensorItem(hingeSensorItemEntity: HingeSensorItemEntity){
        dao.deleteHingeSensorItem(hingeSensorItemEntity)
    }
}