package com.example.hingesensornew.hingesensor.database

class HingeSensorRepository(
    private val dao:HingeSensorItemDao
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

    suspend fun getAllHingeSenorItems():List<HingeSensorItemEntity>{
        return dao.getAllHingeSensorItem()
    }
}