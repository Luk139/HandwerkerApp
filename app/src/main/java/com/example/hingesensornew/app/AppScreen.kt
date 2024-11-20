package com.example.hingesensornew.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun AppScreen(
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
                onHingeSensorClick = {navController.navigate(Routes.HINGESENSOR.value)},
                currentDestination = currentDestination
            )
        },
        modifier = Modifier
    ){
        innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Navigation(
                startDestination,
                navController
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AppScreenPreview() {
    AppScreen()
}