package com.example.hingesensornew.measurement.database

import kotlinx.coroutines.flow.Flow

class MeasurementRepository(
    private val dao: MeasurementDao
) {
    suspend fun insertMeasurement(measurementEntity: MeasurementEntity){
        dao.insertMeasurement(measurementEntity)
    }

    suspend fun deleteMeasurement(measurementEntity: MeasurementEntity){
        dao.deleteMeasurement(measurementEntity)
    }

    suspend fun updateMeasurement(measurementEntity: MeasurementEntity){
        dao.updateMeasurement(measurementEntity)
    }

    fun getMeasurementsWithData():Flow<List<MeasurementAndDistanceItemAndHingeSensorItem>>{
        return dao.getAllMeasurementsWithHingeSensorAndDistance()
    }

    fun getMeasurements():Flow<List<MeasurementEntity>>{
        return dao.getAllMeasurements()
    }
}