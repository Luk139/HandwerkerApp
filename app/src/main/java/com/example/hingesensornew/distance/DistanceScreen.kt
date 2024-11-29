package com.example.hingesensornew.distance

import android.widget.Button
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DistanceScreen(
    onClick:()->Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick
    ) {
        Text("Add Distance")
    }
}