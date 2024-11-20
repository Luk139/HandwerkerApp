package com.example.hingesensornew.measurement.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity

data class MeasurementAndHingeSensorItem(
    @Embedded
    val measurementEntity: MeasurementEntity,
    @Relation(
        parentColumn = "measurementId",
        entityColumn = "measurementOwnerId"
    )
    val hingeSensorItems: List<HingeSensorItemEntity>
)