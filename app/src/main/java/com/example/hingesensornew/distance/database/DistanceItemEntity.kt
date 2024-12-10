package com.example.hingesensornew.distance.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DistanceItemEntity(
        @PrimaryKey(autoGenerate = true)
        val distanceItemId: Int = 0,
        val title:String,
        val distance:Float,
        val measurementOwnerId:Int
    )
