package com.example.hingesensornew.hingesensor.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class HingeSensorItemEntity (
    @PrimaryKey(autoGenerate = true)
    val hingeSensorItemId: Int = 0,
    val title:String,
    val angleDegree:Float,
    val measurementOwnerId:Int
)
