package com.example.hingesensornew.measurement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hingesensornew.measurement.database.MeasurementAndDistanceItemAndHingeSensorItem
import com.example.hingesensornew.measurement.database.MeasurementEntity
import com.example.hingesensornew.measurement.database.MeasurementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MeasurementListScreenViewModel (
    private val measurementRepository: MeasurementRepository
) :ViewModel(){
    private val _measurements = MutableStateFlow<List<MeasurementEntity>>(emptyList())
    val measurements: StateFlow<List<MeasurementEntity>> = _measurements

    init {
        loadMeasurements()
    }

    fun loadMeasurements(){
        viewModelScope.launch {
            measurementRepository.getMeasurements().collect(){
                    measurementList -> _measurements.value = measurementList
            }
        }
    }

}