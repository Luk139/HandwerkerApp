package com.example.hingesensornew.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hingesensornew.distance.DistanceCreationScreenViewModel
import com.example.hingesensornew.distance.DistanceScreenViewModel
import com.example.hingesensornew.hingesensor.HingeSensorCreationScreenViewModel
import com.example.hingesensornew.hingesensor.HingeSensorScreenViewModel
import com.example.hingesensornew.level.LevelScreenViewModel
import com.example.hingesensornew.measurement.MeasurementCreationScreenViewModel
import com.example.hingesensornew.measurement.MeasurementListScreenViewModel
import com.example.hingesensornew.measurement.MeasurementScreenViewModel

@Composable
fun AppScreen(
    measurementScreenViewModel: MeasurementScreenViewModel,
    measurementCreationScreenViewModel: MeasurementCreationScreenViewModel,
    hingeSensorCreationScreenViewModel: HingeSensorCreationScreenViewModel,
    hingeSensorScreenViewModel: HingeSensorScreenViewModel,
    distanceCreationScreenViewModel: DistanceCreationScreenViewModel,
    distanceScreenViewModel: DistanceScreenViewModel,
    measurementListScreenViewModel: MeasurementListScreenViewModel,
    levelScreenViewModel: LevelScreenViewModel,
    modifier: Modifier = Modifier
) {
    val startDestination = Routes.MEASUREMENT.value
    val navController= rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route ?: startDestination
    Scaffold (
        bottomBar = {
            BottomNavigationBar(
                onMeasurementClick =  {navController.navigate(Routes.MEASUREMENT.value)},
                onLevelClick = {navController.navigate(Routes.LEVEL.value)},
                onHingeSensorClick = {navController.navigate(Routes.HINGE_SENSOR.value)},
                onDistanceClick = {navController.navigate(Routes.DISTANCE.value)},
                currentDestination = currentDestination
            )
        },
        floatingActionButton = {
            if(currentDestination == Routes.MEASUREMENT.value){
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.MEASUREMENT_CREATION.value)
                    }){
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        },
        modifier = Modifier
    ){
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Navigation(
                startDestination,
                navController,
                measurementScreenViewModel,
                measurementCreationScreenViewModel,
                hingeSensorCreationScreenViewModel,
                hingeSensorScreenViewModel,
                distanceCreationScreenViewModel,
                distanceScreenViewModel,
                measurementListScreenViewModel,
                levelScreenViewModel
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AppScreenPreview() {
}