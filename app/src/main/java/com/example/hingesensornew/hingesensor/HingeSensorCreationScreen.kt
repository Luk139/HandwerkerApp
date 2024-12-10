package com.example.hingesensornew.hingesensor

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hingesensornew.app.Routes
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity

@Composable
fun HingeSensorCreationScreen(
    angle:Float,
    measurementId:Int,
    hingeSensorCreationScreenViewModel: HingeSensorCreationScreenViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val nameState = remember { mutableStateOf(TextFieldValue()) }
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Winkel Name:",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { newValue ->
                nameState.value = newValue
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val hingeSensorItemEntity = HingeSensorItemEntity(
                        title = nameState.value.text,
                        angleDegree = angle,
                        measurementOwnerId = measurementId
                    )
                    Log.d("NEW Hinge","Added to $measurementId")
                    hingeSensorCreationScreenViewModel.insertHingeSensorEntity(hingeSensorItemEntity)
                    navController.navigate(Routes.HINGE_SENSOR.value)
                },
                modifier = modifier
                    .padding(8.dp)
                    .width(150.dp)
                    .height(60.dp)
            ) {
                Text(text = "Erstellen", fontSize = 18.sp)
            }
            Button(
                onClick = {navController.navigate(Routes.HINGE_SENSOR.value)},
                modifier = modifier
                    .padding(8.dp)
                    .width(150.dp)
                    .height(60.dp)
            ) {
                Text(text = "Abbrechen", fontSize = 18.sp)
            }
        }
    }

}

@Preview
@Composable
private fun HingeSensorCreationScreenPreview() {
    MaterialTheme {
    }
}