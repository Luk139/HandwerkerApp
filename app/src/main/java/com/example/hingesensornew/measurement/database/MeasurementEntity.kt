package com.example.hingesensornew.measurement.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MeasurementEntity(
    @PrimaryKey(autoGenerate = true)
    val measurementId: Int,
    val title:String
)
