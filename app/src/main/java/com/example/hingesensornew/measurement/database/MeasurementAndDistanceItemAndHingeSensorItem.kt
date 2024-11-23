package com.example.hingesensornew.measurement.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hingesensornew.distance.database.DistanceItemEntity
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity

data class MeasurementAndDistanceItemAndHingeSensorItem(
        @Embedded
        val measurementEntity: MeasurementEntity,
        @Relation(
            parentColumn = "measurementId",
            entityColumn = "measurementOwnerId"
        )
        val distanceItems: List<DistanceItemEntity>,
        @Relation(
            parentColumn = "measurementId",
            entityColumn = "measurementOwnerId"
        )
        val hingeSensorItems: List<HingeSensorItemEntity>
)