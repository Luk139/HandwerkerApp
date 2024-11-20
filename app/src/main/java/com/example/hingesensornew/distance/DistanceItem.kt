package com.example.hingesensornew.distance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hingesensornew.distance.database.DistanceItemEntity

@Composable
fun DistanceItem(
    distanceItemEntity: DistanceItemEntity,
    onClick:()->Unit,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Column (
            modifier = Modifier
                .weight(1f)
        ){
            Text(
                text = distanceItemEntity.title,
                modifier = Modifier
                    .padding(8.dp)
            )
            Text(
                text = "Distance: ${distanceItemEntity.distance}",
                modifier = Modifier
                    .padding(8.dp)
            )
        }
        IconButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun DistanceItemPreview() {
    DistanceItem(
        DistanceItemEntity(
                1,
                "Test",
                12.33f,
                2
        ),
        {}
    )
}