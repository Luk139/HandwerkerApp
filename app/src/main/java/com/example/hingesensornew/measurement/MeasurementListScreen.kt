package com.example.hingesensornew.measurement

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.hingesensornew.measurement.database.MeasurementEntity

@Composable
fun MeasurementListScreen(
    measurementListScreenViewModel: MeasurementListScreenViewModel,
    onClick:(MeasurementEntity)->Unit,
    modifier: Modifier = Modifier) {
    LaunchedEffect(Unit) {
        measurementListScreenViewModel.loadMeasurements()
    }
    val measurements by measurementListScreenViewModel.measurements.collectAsState()
    LazyColumn {
        items(measurements){
            item-> SmallMeasurement(item,onClick)
        }
    }
}