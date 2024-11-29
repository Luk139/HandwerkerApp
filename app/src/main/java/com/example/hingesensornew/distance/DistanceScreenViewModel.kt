package com.example.hingesensornew.distance

import android.content.Context
import android.hardware.SensorEventListener
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.android.filament.Engine
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.math.Position
import io.github.sceneview.node.Node
import io.github.sceneview.node.SphereNode

class DistanceScreenViewModel() : ViewModel()
{
    var distance = 0f

    fun drawLineBetweenPoints(
        engine: Engine,
        nodes: MutableList<Node>,
        materialLoader: MaterialLoader,
        startPosition: Position,
        endPosition: Position
    ) {
        // Berechne die Distanz und die Richtung zwischen den beiden Punkten
        val dx = endPosition.x - startPosition.x
        val dy = endPosition.y - startPosition.y
        val dz = endPosition.z - startPosition.z

        val distance = Math.sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()

        // Berechne die Anzahl der Kreise, die entlang der Strecke platziert werden (10 Kreise für je 50 cm)
        val numberOfCircles = (distance / 50f).toInt() * 10

        // Berechne die Inkremente für die Positionierung der Kreise entlang der Linie
        val stepX = dx / numberOfCircles
        val stepY = dy / numberOfCircles
        val stepZ = dz / numberOfCircles

        // Erstelle die Kreise und platziere sie entlang der Linie
        for (i in 0 until numberOfCircles) {
            // Berechne die Position des aktuellen Kreises
            val currentX = startPosition.x + stepX * i
            val currentY = startPosition.y + stepY * i
            val currentZ = startPosition.z + stepZ * i

            // Erstelle einen kleinen Kreis als SphereNode
            val circle = SphereNode(
                engine = engine,
                radius = 0.5f,  // kleiner Kreis
                materialInstance = materialLoader.createColorInstance(
                    color = Color.Yellow,  // Farbe der Kreise
                    metallic = 0.0f,
                    roughness = 0.0f,
                    reflectance = 0.0f
                )
            ).apply {
                // Positioniere den Kreis entlang der Linie
                transform(position = Position(currentX, currentY, currentZ))
            }

            // Füge den Kreis zur Liste der Knoten hinzu
            nodes.add(circle)
        }
    }

    fun calculateDistance(position1: Position, position2: Position): Float {
        val dx = position2.x - position1.x
        val dy = position2.y - position1.y
        val dz = position2.z - position1.z
        val distance = Math.sqrt(((dx * dx + dy * dy + dz * dz).toDouble()))
        this.distance = (distance * 100).toFloat()
        return (distance * 100).toFloat()
    }

    fun returnDistance(): Float {
        return distance
    }
}