package com.example.hingesensornew

import android.content.Context
import android.hardware.*
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.hingesensornew.distance.database.DistanceItemDatabase
import com.example.hingesensornew.distance.database.DistanceItemRepository
import com.example.hingesensornew.hingesensor.database.HingeSensorItemDatabase
import com.example.hingesensornew.hingesensor.database.HingeSensorRepository
import com.example.hingesensornew.measurement.database.MeasurementDatabase
import com.example.hingesensornew.measurement.database.MeasurementRepository
import com.example.hingesensornew.ui.theme.HingeSensorNewTheme
import kotlin.math.acos
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {
    // Initialize Databases
    val measurementDatabase = MeasurementDatabase.getDatabase(applicationContext, lifecycleScope)
    val hingeSensorItemDatabase = HingeSensorItemDatabase.getDatabase(applicationContext, lifecycleScope)
    val distanceItemDatabase = DistanceItemDatabase.getDatabase(applicationContext, lifecycleScope)

    //Initialize DAOs
    val measurementDao = measurementDatabase.measurementDao()
    val distanceItemDao = distanceItemDatabase.distanceItemDao()
    val hingeSensorItemDao = hingeSensorItemDatabase.hingeSensorItemDao()

    //Initialize Repositories
    val measurementRepository = MeasurementRepository(measurementDao)
    val distanceItemRepository = DistanceItemRepository(distanceItemDao)
    val hingeSensorRepository = HingeSensorRepository(hingeSensorItemDao)

    private lateinit var sensorManager: SensorManager
    private var hingeAngleSensor: Sensor? = null
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var foldAngle: Float by mutableFloatStateOf(0f)
    private var accelerationData = FloatArray(3)
    private var gyroscopeData = FloatArray(3)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        hingeAngleSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HINGE_ANGLE)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        hingeAngleSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscope?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        setContent {
            HingeSensorNewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Fold Angle: ${foldAngle.toInt()}°",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_HINGE_ANGLE -> {
                val hingeAngle = event.values[0]
                when {
                    hingeAngle == 90f -> {
                        foldAngle = calculateFoldAngle()
                    }
                    hingeAngle == 0f || hingeAngle == 180f -> {
                        foldAngle = hingeAngle
                    }
                    else -> {
                        foldAngle = hingeAngle
                    }
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                accelerationData = event.values.clone()
                if (hingeAngleSensor != null) {
                    foldAngle = calculateFoldAngle()
                }
            }
            Sensor.TYPE_GYROSCOPE -> {
                gyroscopeData = event.values.clone()
            }
        }
    }

    private fun calculateFoldAngle(): Float {
        val xAcc = accelerationData[0]
        val yAcc = accelerationData[1]
        val zAcc = accelerationData[2]

        val accelerationMagnitude = sqrt(xAcc * xAcc + yAcc * yAcc + zAcc * zAcc)
        val normalizedZ = zAcc / accelerationMagnitude

        return Math.toDegrees(acos(normalizedZ.toDouble())).toFloat()
    }



    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        hingeAngleSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscope?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HingeSensorNewTheme {
        Greeting("Android")
    }
}
