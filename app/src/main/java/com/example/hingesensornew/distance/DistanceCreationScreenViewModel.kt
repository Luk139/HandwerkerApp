package com.example.hingesensornew.distance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hingesensornew.distance.database.DistanceItemEntity
import com.example.hingesensornew.distance.database.DistanceItemRepository
import kotlinx.coroutines.launch

class DistanceCreationScreenViewModel(
    private val distanceItemRepository: DistanceItemRepository
) :ViewModel() {
    fun insertDistanceEntity(distanceItemEntity: DistanceItemEntity){
        viewModelScope.launch{
            distanceItemRepository.insertDistanceItem(distanceItemEntity)
        }
    }
}