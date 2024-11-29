package com.example.hingesensornew.hingesensor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import com.example.hingesensornew.hingesensor.database.HingeSensorRepository
import kotlinx.coroutines.launch

class HingeSensorCreationScreenViewModel(
    private val hingeSensorRepository: HingeSensorRepository
): ViewModel(){
    fun insertHingeSensorEntity(hingeSensorItemEntity: HingeSensorItemEntity){
        viewModelScope.launch {
            hingeSensorRepository.insertHingeSensorItem(hingeSensorItemEntity)
        }
    }
}