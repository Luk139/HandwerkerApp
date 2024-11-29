package com.example.hingesensornew.measurement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hingesensornew.measurement.database.MeasurementEntity
import com.example.hingesensornew.ui.theme.HingeSensorNewTheme

@Composable
fun SmallMeasurement(
    measurementEntity: MeasurementEntity,
    onClick:(MeasurementEntity)->Unit,
    modifier: Modifier = Modifier
) {
    Box(
        Modifier
        .padding(8.dp)
        .background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
        )
        .fillMaxWidth()
        .clickable(
            onClick = {onClick(measurementEntity)}
        )){
        Text(
        text = measurementEntity.title,
        modifier = Modifier
            .padding(8.dp)
    )}

}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun SmallMeasurementPreview() {
    val measurementEntity = MeasurementEntity(
        1,
        "Test"
    )
    HingeSensorNewTheme {
        SmallMeasurement(
            measurementEntity,
            {}
        )
    }
}