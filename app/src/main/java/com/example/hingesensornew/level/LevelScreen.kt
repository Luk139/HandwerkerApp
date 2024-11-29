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
fun LevelScreen(levelScreenViewModel: LevelScreenViewModel
) {
    val maxTranslation = 250f // Maximale Bewegung

    // Zielwerte für horizontale und vertikale Bewegungen berechnen
    val targetHorizontalX = when {
        Math.abs(levelScreenViewModel.angleY.value) < 0.01 -> 0f
        Math.abs(levelScreenViewModel.angleY.value) < 20 -> (levelScreenViewModel.angleY.value * maxTranslation / 20).toFloat()
        else -> if (levelScreenViewModel.angleY.value > 0) maxTranslation else -maxTranslation
    }

    val targetVerticalY = when {
        Math.abs(levelScreenViewModel.angleX.value - 90) < 0.01 -> 0f
        Math.abs(levelScreenViewModel.angleX.value - 90) < 20 -> ((levelScreenViewModel.angleX.value - 90) * maxTranslation / 20).toFloat()
        else -> if (levelScreenViewModel.angleX.value > 90) maxTranslation else -maxTranslation
    }

    // Animatable-Objekte für die Animation der Offsets
    val horizontalOffset = remember { Animatable(0f) }
    val verticalOffset = remember { Animatable(0f) }

    // Animationen auslösen, wenn die Zielwerte sich ändern
    LaunchedEffect(targetHorizontalX) {
        if (!levelScreenViewModel.isVertical.value) {
            horizontalOffset.animateTo(targetHorizontalX)
        }
    }

    LaunchedEffect(targetVerticalY) {
        if (levelScreenViewModel.isVertical.value) {
            verticalOffset.animateTo(targetVerticalY)
        }
    }

    // Sicherstellen, dass nur der richtige Offset animiert wird (horizontal oder vertikal)
    LaunchedEffect(levelScreenViewModel.isVertical) {
        if (levelScreenViewModel.isVertical.value) {
            verticalOffset.snapTo(targetVerticalY) // Verwende snapTo, wenn keine Animation notwendig ist
        } else {
            horizontalOffset.snapTo(targetHorizontalX) // Verwende snapTo, wenn keine Animation notwendig ist
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
                        x = if (!levelScreenViewModel.isVertical.value) horizontalOffset.value.dp else 0.dp,
                        y = if (levelScreenViewModel.isVertical.value) verticalOffset.value.dp else 0.dp
                    )
            )
        }
    }
}

