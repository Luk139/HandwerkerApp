package com.example.hingesensornew.measurement.database

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

    suspend fun getMeasurementsWithDistanceAndHingeSensorItems():List<MeasurementAndDistanceItemAndHingeSensorItem>{
        return dao.getAllMeasurementsWithDistanceItemAndHingeSensorItem()
    }
}