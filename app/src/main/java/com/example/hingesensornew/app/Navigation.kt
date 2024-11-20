package com.example.hingesensornew.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.createGraph

@Composable
fun Navigation(
    startDestination: String,
    navController:NavHostController,
    modifier: Modifier = Modifier
) {
    val navGraph = remember(navController) {
        navController.createGraph(startDestination = startDestination){
            composable(Routes.MEASUREMENT.value){

            }
            composable(Routes.HINGESENSOR.value){

            }
            composable(Routes.HINGESENSOR.value){

            }
        }
    }
}

@Preview
@Composable
private fun NavigationPreview() {

}