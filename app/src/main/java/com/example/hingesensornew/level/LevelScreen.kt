package com.example.hingesensornew.level

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.SliderDefaults

@Composable
fun LevelScreen(levelScreenViewModel: LevelScreenViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp)
        ) {
            val adjustedAngleX = when {
                levelScreenViewModel.angleX.value > 190 -> levelScreenViewModel.angleX.value - 180
                levelScreenViewModel.angleX.value < -10 -> levelScreenViewModel.angleX.value + 180
                else -> levelScreenViewModel.angleX.value
            }

            val normalizedX = ((adjustedAngleX - 90f) / 40f).coerceIn(-1.0, 1.0)

            val adjustedAngleY = when {
                levelScreenViewModel.angleY.value > 100 -> levelScreenViewModel.angleY.value - 180
                levelScreenViewModel.angleY.value < -100 -> levelScreenViewModel.angleY.value + 180
                else -> levelScreenViewModel.angleY.value
            }

            val normalizedY = (adjustedAngleY / 20f).coerceIn(-1.0, 1.0)


            Slider(
                value = normalizedX.toFloat(),
                onValueChange = {},
                valueRange = -1f..1f,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.Center)
                    .padding(vertical = 8.dp)
                    .rotate(90f)
                    .alpha(if (levelScreenViewModel.isVertical.value) 1f else 0f)
            )

            if (!levelScreenViewModel.isVertical.value) {
                Slider(
                    value = normalizedY.toFloat(),
                    onValueChange = {},
                    valueRange = -1f..1f,
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                    .align(Alignment.Center)
            )
        }
    }
}
