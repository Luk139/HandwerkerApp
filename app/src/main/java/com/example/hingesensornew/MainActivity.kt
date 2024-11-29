package com.example.hingesensornew

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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hingesensornew.ui.theme.HingeSensorNewTheme

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor

    private var calGyroX = 0.0
    private var calGyroY = 0.0
    private var calGyroZ = 0.0

    private val currentAngleState = mutableStateOf(180) // Startwert für den Winkel
    private val isCalibratingState = mutableStateOf(false)
    private val countdownTextState = mutableStateOf("Countdown: 3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        setContent {
            HingeSensorNewTheme {
                MainActivityScreen(
                    currentAngle = currentAngleState.value,
                    isCalibrating = isCalibratingState.value,
                    countdownText = countdownTextState.value,
                    onCalibrate = { startCalibration() },
                    onOpenCamera = {
                        val intent = Intent(this, CameraActivity::class.java)
                        startActivity(intent)
                    },
                    onOpenSpiritLevel = {
                        val intent = Intent(this, SpiritLevelActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }

        startCalibration()
    }

    private fun startCalibration() {
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

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

@Composable
fun MainActivityScreen(
    currentAngle: Int,
    isCalibrating: Boolean,
    countdownText: String,
    onCalibrate: () -> Unit,
    onOpenCamera: () -> Unit,
    onOpenSpiritLevel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Aktueller Winkel: $currentAngle°",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onCalibrate) {
            Text("Kalibrieren")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isCalibrating) {
            Text(
                text = countdownText,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onOpenCamera) {
            Text("Zur Kamera-Ansicht")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onOpenSpiritLevel) {
            Text("Zur Wasserwaage")
        }
    }
}
