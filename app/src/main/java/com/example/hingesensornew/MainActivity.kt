package com.example.hingesensornew

import android.content.Context
import android.hardware.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hingesensornew.app.AppScreen
import com.example.hingesensornew.distance.DistanceCreationScreenViewModel

import com.example.hingesensornew.distance.database.DistanceItemRepository
import com.example.hingesensornew.hingesensor.HingeSensorCreationScreenViewModel
import com.example.hingesensornew.hingesensor.HingeSensorScreenViewModel
import com.example.hingesensornew.hingesensor.database.HingeSensorItemDatabase
import com.example.hingesensornew.hingesensor.database.HingeSensorRepository
import com.example.hingesensornew.level.LevelScreenViewModel
import com.example.hingesensornew.measurement.MeasurementCreationScreenViewModel
import com.example.hingesensornew.measurement.MeasurementListScreenViewModel
import com.example.hingesensornew.measurement.MeasurementScreenViewModel
import com.example.hingesensornew.measurement.database.MeasurementDatabase
import com.example.hingesensornew.measurement.database.MeasurementRepository

import com.example.hingesensornew.ui.theme.HingeSensorNewTheme
import kotlin.math.acos
import kotlin.math.sqrt

class MainActivity : ComponentActivity(){
    // Initialize Databases


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val measurementDatabase = MeasurementDatabase.getDatabase(applicationContext, lifecycleScope)

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //Initialize DAOs
        Log.d("Initialization","Initilaize Data")
        val measurementDao = measurementDatabase.measurementDao()

        //Initialize Repositories
        val measurementRepository = MeasurementRepository(measurementDao)
        val distanceItemRepository = DistanceItemRepository(measurementDao)
        val hingeSensorRepository = HingeSensorRepository(measurementDao)

        //Initialize ViewModels
        val measurementScreenViewModel = MeasurementScreenViewModel(
            measurementRepository,
            distanceItemRepository,
            hingeSensorRepository
        )
        val measurementCreationScreenViewModel = MeasurementCreationScreenViewModel(
            measurementRepository
        )
        val distanceCreationScreenViewModel = DistanceCreationScreenViewModel(
            distanceItemRepository
        )
        val hingeSensorCreationScreenViewModel = HingeSensorCreationScreenViewModel(
            hingeSensorRepository
        )
        val hingeSensorScreenViewModel = HingeSensorScreenViewModel(sensorManager)
        val measurementListScreenViewModel = MeasurementListScreenViewModel(
            measurementRepository
        )
        val levelScreenViewModel = LevelScreenViewModel(this)
        Log.d("Initialization","Initilaize Data Finished")
        setContent {
            HingeSensorNewTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScreen(
                        measurementScreenViewModel,
                        measurementCreationScreenViewModel,
                        hingeSensorCreationScreenViewModel,
                        hingeSensorScreenViewModel,
                        distanceCreationScreenViewModel,
                        measurementListScreenViewModel,
                        levelScreenViewModel
                    )
                }
            }
        }
    }



}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HingeSensorNewTheme {

    }
}
