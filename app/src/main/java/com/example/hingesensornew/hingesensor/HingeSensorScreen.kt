package com.example.hingesensornew.hingesensor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HingeSensorScreen(
    currentAngle: Int,
    isCalibrating: Boolean,
    countdownText: String,
    onCalibrate: () -> Unit,
    onAddToMeasurement: () -> Unit,
) {
    LaunchedEffect(Unit) {
        onCalibrate()
    }

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

        Button(onClick = onAddToMeasurement) {
            Text("Zur Messung hinzufügen")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}