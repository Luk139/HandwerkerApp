package com.example.hingesensornew.app

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.hingesensornew.R

@Composable
fun BottomNavigationBar(
    onMeasurementClick: ()-> Unit,
    onHingeSensorClick: ()-> Unit,
    onLevelClick:()->Unit,
    currentDestination: String,
    modifier: Modifier = Modifier
) {
    NavigationBar (
        modifier = Modifier
    ){
        NavigationBarItem(
            selected = currentDestination == Routes.MEASUREMENT.value,
            onClick = onMeasurementClick,
            icon= {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_list_24),
                    contentDescription = "Measurements"
                )
            }

        )
        NavigationBarItem(
            selected = currentDestination == Routes.HINGESENSOR.value,
            onClick = onHingeSensorClick,
            icon= {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.architecture_24dp_000000_fill0_wght400_grad0_opsz24),
                    contentDescription = "Hinge Sensor"
                )
            }
        )
        NavigationBarItem(
            selected = currentDestination == Routes.LEVEL.value,
            onClick = onLevelClick,
            icon= {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.tools_level_24dp_000000_fill0_wght400_grad0_opsz24),
                    contentDescription = "Level"
                )
            }
        )
    }
}

@Preview
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        {},
        {},
        {},
        currentDestination = Routes.LEVEL.value
    )
}