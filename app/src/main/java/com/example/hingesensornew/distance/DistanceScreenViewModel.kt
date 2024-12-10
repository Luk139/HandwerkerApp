package com.example.hingesensornew.distance

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.android.filament.Engine
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.math.Position
import io.github.sceneview.node.Node
import io.github.sceneview.node.SphereNode

class DistanceScreenViewModel() : ViewModel() {
    var distance = 0f

    fun drawLineBetweenPoints(
        engine: Engine,
        nodes: MutableList<Node>,
        materialLoader: MaterialLoader,
        startPosition: Position,
        endPosition: Position
    ) {
        val dx = endPosition.x - startPosition.x
        val dy = endPosition.y - startPosition.y
        val dz = endPosition.z - startPosition.z

        val numberOfPoints = (calculateDistance(startPosition, endPosition) / 5).toInt().coerceIn(0, 25)

        val stepX = dx / numberOfPoints
        val stepY = dy / numberOfPoints
        val stepZ = dz / numberOfPoints

        for (i in 0.until(numberOfPoints)) {
            // Berechne die Position des aktuellen Punktes
            val currentX = startPosition.x + stepX * i
            val currentY = startPosition.y + stepY * i
            val currentZ = startPosition.z + stepZ * i

            Log.d("Position", "X: $currentX, Y: $currentY, Z: $currentZ")

            val circle = SphereNode(
                engine = engine,
                radius = 0.01f,
                materialInstance = materialLoader.createColorInstance(
                    color = Color.Yellow,
                    metallic = 0.0f,
                    roughness = 0.0f,
                    reflectance = 0.0f
                )
            ).apply {
                transform(position = Position(currentX, currentY, currentZ))
            }

            nodes.add(2 + i, circle)
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


