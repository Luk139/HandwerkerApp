package com.example.hingesensornew.hingesensor

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import com.example.hingesensornew.CameraActivity
import com.example.hingesensornew.SpiritLevelActivity
import com.example.hingesensornew.ui.theme.HingeSensorNewTheme

class HingeSensorScreenViewModel(var sensorManager: SensorManager) : SensorEventListener, ViewModel(){

    private var gyroscope: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

    private var calGyroX = 0.0
    private var calGyroY = 0.0
    private var calGyroZ = 0.0

    public val currentAngleState = mutableStateOf(180) // Startwert für den Winkel
    public val isCalibratingState = mutableStateOf(false)
    public val countdownTextState = mutableStateOf("Countdown: 3")

    init{
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    public fun startCalibration() {
        isCalibratingState.value = true
        countdownTextState.value = "Countdown: 3"

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                countdownTextState.value = "Countdown: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                countdownTextState.value = "Kalibrierung abgeschlossen"
                isCalibratingState.value = false
                calibrateGyroscope()
            }
        }.start()
    }

    private fun calibrateGyroscope() {
        calGyroX = 0.0
        calGyroY = 0.0
        calGyroZ = 0.0
        currentAngleState.value = 180
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
            if (isCalibratingState.value) return

            val gyroY = event.values[1] - calGyroY
            val newAngle = currentAngleState.value + (gyroY * 13.5).toInt()
            currentAngleState.value = newAngle.coerceIn(0, 180)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    public fun returnAngle(): MutableState<Int> {
        return currentAngleState
    }
}