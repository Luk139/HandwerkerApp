package com.example.hingesensornew.distance.database

class DistanceItemRepository (
    private val dao: DistanceItemDao
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

    suspend fun getAllDistanceItems(): List<DistanceItemEntity>{
        return dao.getAllDistanceItems()
    }
}