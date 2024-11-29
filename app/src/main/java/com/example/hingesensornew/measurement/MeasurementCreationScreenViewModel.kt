package com.example.hingesensornew.measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hingesensornew.measurement.database.MeasurementEntity
import com.example.hingesensornew.measurement.database.MeasurementRepository
import kotlinx.coroutines.launch

class MeasurementCreationScreenViewModel(
    private val measurementRepository: MeasurementRepository
): ViewModel(){

    fun insertMeasurement(measurementEntity: MeasurementEntity){
        viewModelScope.launch {
            measurementRepository.insertMeasurement(measurementEntity)
        }
    }
}