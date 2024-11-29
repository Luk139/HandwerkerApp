package com.example.hingesensornew

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
fun SpiritLevelScreen(
    x: Float,
    y: Float,
    angleX: Double,
    angleY: Double,
    isVertical: Boolean
) {
    val maxTranslation = 250f // Maximale Bewegung

    // Zielwerte für horizontale und vertikale Bewegungen berechnen
    val targetHorizontalX = when {
        Math.abs(angleY) < 0.01 -> 0f
        Math.abs(angleY) < 20 -> (angleY * maxTranslation / 20).toFloat()
        else -> if (angleY > 0) maxTranslation else -maxTranslation
    }

    val targetVerticalY = when {
        Math.abs(angleX - 90) < 0.01 -> 0f
        Math.abs(angleX - 90) < 20 -> ((angleX - 90) * maxTranslation / 20).toFloat()
        else -> if (angleX > 90) maxTranslation else -maxTranslation
    }

    // Animatable-Objekte für die Animation der Offsets
    val horizontalOffset = remember { Animatable(0f) }
    val verticalOffset = remember { Animatable(0f) }

    // Animationen auslösen, wenn die Zielwerte sich ändern
    LaunchedEffect(targetHorizontalX) {
        if (!isVertical) {
            horizontalOffset.animateTo(targetHorizontalX)
        }
    }

    LaunchedEffect(targetVerticalY) {
        if (isVertical) {
            verticalOffset.animateTo(targetVerticalY)
        }
    }

    // Sicherstellen, dass nur der richtige Offset animiert wird (horizontal oder vertikal)
    LaunchedEffect(isVertical) {
        if (isVertical) {
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
            text = "Neigung: X=${Math.round(angleX)}°, Y=${Math.round(angleY)}°" +
                    if (isVertical) " (Vertikal)" else " (Horizontal)",
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
                        x = if (!isVertical) horizontalOffset.value.dp else 0.dp,
                        y = if (isVertical) verticalOffset.value.dp else 0.dp
                    )
            )
        }
    }
}

