package com.example.hingesensornew.hingesensor

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HingeSensorScreen(
    onClick:()->Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick
    ) {
        Text("Add HingeSensor")
    }
}