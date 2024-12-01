package com.example.hingesensornew.level

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Animatable

@Composable
fun LevelScreen(levelScreenViewModel: LevelScreenViewModel) {
    val maxTranslation = 250f // Maximale Bewegung

    // Zielwerte für horizontale und vertikale Bewegungen berechnen
    val targetHorizontalX = 90f

    val targetVerticalY = 90f

    // Animatable-Objekte für die Animation der Offsets
    val offset = remember { Animatable(0f) }

    // Animation entsprechend dem Modus (horizontal oder vertikal)
    LaunchedEffect(levelScreenViewModel.isVertical.value, targetHorizontalX, targetVerticalY) {
        if (levelScreenViewModel.isVertical.value) {
            // Vertikale Animation
            offset.animateTo(targetVerticalY)
        } else {
            // Horizontale Animation
            offset.animateTo(targetHorizontalX)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Neigung: X=${Math.round(levelScreenViewModel.angleX.value)}°, Y=${Math.round(levelScreenViewModel.angleY.value)}°" +
                    if (levelScreenViewModel.isVertical.value) " (Vertikal)" else " (Horizontal)",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Horizontale Linie
            Box(
                modifier = Modifier
                    .size(width = 200.dp, height = 2.dp)
                    .background(Color.Gray)
                    .align(Alignment.Center)
            )

            // Vertikale Linie
            Box(
                modifier = Modifier
                    .size(width = 2.dp, height = 200.dp)
                    .background(Color.Gray)
                    .align(Alignment.Center)
            )

            // Zentrale Box
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(1.dp, Color.Black)
                    .align(Alignment.Center)
            )

            // Grüne Box, die sich entlang der Achse bewegt
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Green)
                    .align(Alignment.Center)
                    .offset(
                        x = if (!levelScreenViewModel.isVertical.value) offset.value.dp else 0.dp,
                        y = if (levelScreenViewModel.isVertical.value) offset.value.dp else 0.dp
                    )
            )
        }
    }
}