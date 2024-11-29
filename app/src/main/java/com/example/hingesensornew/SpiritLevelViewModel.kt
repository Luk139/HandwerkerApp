package com.example.hingesensornew

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SpiritLevelViewModel(context: Context) : ViewModel(), SensorEventListener {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val x = mutableStateOf(0f)
    val y = mutableStateOf(0f)
    val angleX = mutableStateOf(0.0)
    val angleY = mutableStateOf(0.0)
    val isVertical = mutableStateOf(false)

    init {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            val z = event.values[2]

            x.value = event.values[0]
            y.value = event.values[1]
            angleX.value = Math.atan2(y.value.toDouble(), z.toDouble()) * (180 / Math.PI)
            angleY.value = Math.atan2(x.value.toDouble(), z.toDouble()) * (180 / Math.PI)

            isVertical.value = Math.abs(z) < 8
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}
