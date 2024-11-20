package com.example.hingesensornew.measurement

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hingesensornew.hingesensor.HingeSensorItem
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import com.example.hingesensornew.measurement.database.MeasurementAndHingeSensorItem
import com.example.hingesensornew.measurement.database.MeasurementEntity

@Composable
fun Measurement(
    measurementAndHingeSensorItem: MeasurementAndHingeSensorItem,
    onMeasurementAddClick:()->Unit,
    onMeasurementDeleteClick:()->Unit,
    onHingeSensorDeleteClick:()->Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = modifier
            .padding(8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { isExpanded = !isExpanded }
    ){
        Row (
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(
                text = measurementAndHingeSensorItem.measurementEntity.title,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onMeasurementAddClick
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Measurements")
            }
            IconButton(
                onClick = onMeasurementDeleteClick
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Measurements")
            }
        }
        if (isExpanded) {
            // Line between title and description
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                thickness = 1.dp,
                color = Color.Black
            )
            for(hingeSensorEntity in measurementAndHingeSensorItem.hingeSensorItems){
                HingeSensorItem(
                    hingeSensorEntity,
                    onHingeSensorDeleteClick
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MeasurementItemPreview() {

    val list = mutableListOf<HingeSensorItemEntity>()
    list.add(
            HingeSensorItemEntity(
                1,
                "Test 1",
                12.33f,
                2
            )
    )
    list.add(
        HingeSensorItemEntity(
            1,
            "Test 2",
            15.33f,
            2
        )
    )
    MaterialTheme {
        Measurement(
            MeasurementAndHingeSensorItem(
                MeasurementEntity(
                    1,
                    "Test"
                ),
                list
            ),
            {},
            {},
            {}
        )
    }
}