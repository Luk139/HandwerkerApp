package com.example.hingesensornew.measurement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hingesensornew.distance.database.DistanceItemEntity
import com.example.hingesensornew.distance.database.DistanceItemRepository
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import com.example.hingesensornew.hingesensor.database.HingeSensorRepository
import com.example.hingesensornew.measurement.database.MeasurementAndDistanceItemAndHingeSensorItem
import com.example.hingesensornew.measurement.database.MeasurementEntity
import com.example.hingesensornew.measurement.database.MeasurementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MeasurementScreenViewModel(
    private val measurementRepository: MeasurementRepository,
    private val distanceItemRepository: DistanceItemRepository,
    private val hingeSensorRepository: HingeSensorRepository
) : ViewModel(){
    private val _measurements = MutableStateFlow<List<MeasurementAndDistanceItemAndHingeSensorItem>>(emptyList())
    val measurements: StateFlow<List<MeasurementAndDistanceItemAndHingeSensorItem>> = _measurements

    init {
        loadMeasurements()
    }

    fun loadMeasurements(){
        viewModelScope.launch {
            measurementRepository.getMeasurementsWithData().collect { measurements ->
                _measurements.value =measurements
            }
        }
    }

    fun deleteMeasurement(
        measurementAndDistanceItemAndHingeSensorItem: MeasurementAndDistanceItemAndHingeSensorItem
    ){
        val measurement = measurementAndDistanceItemAndHingeSensorItem.measurementEntity
        val distances = measurementAndDistanceItemAndHingeSensorItem.distanceItems
        val hingeSensorItems = measurementAndDistanceItemAndHingeSensorItem.hingeSensorItemEntities
        for(distance in distances){
            viewModelScope.launch {
                Log.d("Distance","Delete")
                distanceItemRepository.deleteDistanceItem(distance)
            }
        }
        for(hingeSensorItem in hingeSensorItems){
            viewModelScope.launch {
                Log.d("Hinge","Delete")
                hingeSensorRepository.deleteHingeSensorItem(hingeSensorItem)
            }
        }
        viewModelScope.launch {
            measurementRepository.deleteMeasurement(measurement)
        }
    }

    fun deleteDistanceEntity(
        distanceItemEntity: DistanceItemEntity
    ){
        viewModelScope.launch {
            distanceItemRepository.deleteDistanceItem(distanceItemEntity)
        }
    }

    fun deleteHingeSensorEntity(
        hingeSensorItemEntity: HingeSensorItemEntity
    ){
        viewModelScope.launch {
            hingeSensorRepository.deleteHingeSensorItem(hingeSensorItemEntity)
        }
    }
}