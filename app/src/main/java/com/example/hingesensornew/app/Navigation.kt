package com.example.hingesensornew.app

import MeasurementScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.example.hingesensornew.distance.DistanceCreationScreen
import com.example.hingesensornew.distance.DistanceCreationScreenViewModel
import com.example.hingesensornew.distance.DistanceScreen
import com.example.hingesensornew.hingesensor.HingeSensorCreationScreen
import com.example.hingesensornew.hingesensor.HingeSensorCreationScreenViewModel
import com.example.hingesensornew.hingesensor.HingeSensorScreen
import com.example.hingesensornew.hingesensor.HingeSensorScreenViewModel
import com.example.hingesensornew.level.LevelScreen
import com.example.hingesensornew.measurement.MeasurementCreationScreen
import com.example.hingesensornew.measurement.MeasurementCreationScreenViewModel
import com.example.hingesensornew.measurement.MeasurementListScreen
import com.example.hingesensornew.measurement.MeasurementListScreenViewModel
import com.example.hingesensornew.measurement.MeasurementScreenViewModel

@Composable
fun Navigation(
    startDestination: String,
    navController:NavHostController,
    measurementScreenViewModel: MeasurementScreenViewModel,
    measurementCreationScreenViewModel: MeasurementCreationScreenViewModel,
    hingeSensorCreationScreenViewModel: HingeSensorCreationScreenViewModel,
    hingeSensorScreenViewModel: HingeSensorScreenViewModel,
    distanceCreationScreenViewModel: DistanceCreationScreenViewModel,
    measurementListScreenViewModel: MeasurementListScreenViewModel,
    modifier: Modifier = Modifier
) {
    var navigatedFrom by rememberSaveable {
        mutableStateOf("")
    }
    var measurementOwnerId by rememberSaveable {
        mutableIntStateOf(0)
    }
    var angle by rememberSaveable {
        mutableFloatStateOf(0.0f)
    }
    var distance by rememberSaveable {
        mutableFloatStateOf(0.0f)
    }
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = startDestination){
            composable(Routes.MEASUREMENT.value){
                MeasurementScreen(
                    measurementScreenViewModel
                )
            }
            composable(Routes.HINGE_SENSOR.value){
                HingeSensorScreen(
                    currentAngle = hingeSensorScreenViewModel.currentAngleState.value,
                    isCalibrating = hingeSensorScreenViewModel.isCalibratingState.value,
                    countdownText = hingeSensorScreenViewModel.countdownTextState.value,
                    onCalibrate = { hingeSensorScreenViewModel.startCalibration() },
                    onAddToMeasurement = {
                        angle = hingeSensorScreenViewModel.returnAngle().value.toFloat()
                        navigatedFrom = Routes.HINGE_SENSOR.value
                        navController.navigate(Routes.MEASUREMENT_SELECTION.value)
                    }
                )
            }
            composable(Routes.LEVEL.value){
                LevelScreen()
            }
            composable(Routes.DISTANCE.value){
                DistanceScreen(
                    {
                        navigatedFrom = Routes.DISTANCE.value
                        distance = 12.0f
                        navController.navigate(Routes.MEASUREMENT_SELECTION.value)
                    }
                )
            }
            composable(Routes.MEASUREMENT_CREATION.value){
                MeasurementCreationScreen(
                    measurementCreationScreenViewModel,
                    navController
                )
            }
            composable(Routes.HINGE_SENSOR_CREATION.value){
                HingeSensorCreationScreen(
                    angle,
                    measurementOwnerId,
                    hingeSensorCreationScreenViewModel,
                    navController
                )
            }
            composable(Routes.DISTANCE_CREATION.value){
                DistanceCreationScreen(
                    distance,
                    measurementOwnerId,
                    distanceCreationScreenViewModel,
                    navController
                )
            }
            composable(Routes.MEASUREMENT_SELECTION.value){
                MeasurementListScreen(
                    measurementListScreenViewModel,
                    {
                        measurement->
                        measurementOwnerId = measurement.measurementId
                        if(navigatedFrom == Routes.HINGE_SENSOR.value)
                            navController.navigate(Routes.HINGE_SENSOR_CREATION.value)
                        else
                            navController.navigate(Routes.DISTANCE_CREATION.value)
                    }
                )
            }
        }
    }
    NavHost(navController,navGraph)
}

@Preview
@Composable
private fun NavigationPreview() {

}