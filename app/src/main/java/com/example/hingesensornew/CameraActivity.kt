package com.example.hingesensornew

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.ARScene
import io.github.sceneview.node.Node
import io.github.sceneview.math.Position
import io.github.sceneview.node.SphereNode
import com.google.android.filament.Engine
import com.google.ar.core.Config
import com.google.ar.core.Frame
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberView
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberCollisionSystem

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ARScreen()
        }
    }
}

@Composable
fun ARScreen() {
    val engine = rememberEngine()
    val view = rememberView(engine)
    val renderer = rememberRenderer(engine)
    val scene = rememberScene(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val collisionSystem = rememberCollisionSystem(view)

    var currentFrame by remember { mutableStateOf<Frame?>(null) }

    val spherePositions = remember { mutableListOf<Position>() }
    var distance by remember { mutableStateOf<Float>(0f) }

    val nodes = rememberNodes()

    // Variable für die Linie
    var lineNode: Node? by remember { mutableStateOf(null) }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        view = view,
        renderer = renderer,
        scene = scene,
        materialLoader = materialLoader,
        collisionSystem = collisionSystem,
        isOpaque = true,
        planeRenderer = false,
        sessionConfiguration = { session, config ->
            config.depthMode = Config.DepthMode.AUTOMATIC
            config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
        childNodes = nodes,
        onTouchEvent = { event, _ ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val hitResults = currentFrame?.hitTest(event.x, event.y)
                hitResults?.firstOrNull()?.let { hitResult ->
                    val hitPosition = hitResult.hitPose.translation
                    val positionInFrontOfCamera = Position(
                        x = hitPosition[0],
                        y = hitPosition[1],
                        z = hitPosition[2]
                    )

                    if (spherePositions.size < 2) {
                        // Füge einen neuen Punkt hinzu
                        val newSphere = SphereNode(
                            engine = engine,
                            radius = 0.025f,
                            materialInstance = materialLoader.createColorInstance(
                                color = Color.Black,
                                metallic = 0.0f,
                                roughness = 0.0f,
                                reflectance = 0.0f
                            )
                        ).apply {
                            transform(position = positionInFrontOfCamera)
                        }
                        nodes.add(newSphere)
                        spherePositions.add(positionInFrontOfCamera)

                        if (spherePositions.size == 2) {
                            // Berechne die Distanz
                            distance = calculateDistance(spherePositions[0], spherePositions[1])

                            // Erstelle eine Linie zwischen den Punkten
                            drawLineBetweenPoints(engine, nodes, materialLoader, spherePositions[0], spherePositions[1])
                        }
                    } else {
                        // Entferne den ersten Punkt und ersetze ihn durch den neuen
                        spherePositions[0] = positionInFrontOfCamera
                        val firstSphere = nodes.firstOrNull() as? SphereNode
                        firstSphere?.let {
                            nodes.remove(it)
                        }

                        val newSphere = SphereNode(
                            engine = engine,
                            radius = 0.025f,
                            materialInstance = materialLoader.createColorInstance(
                                color = Color.Black,
                                metallic = 0.0f,
                                roughness = 0.0f,
                                reflectance = 0.0f
                            )
                        ).apply {
                            transform(position = positionInFrontOfCamera)
                        }
                        nodes.add(newSphere)

                        // Berechne erneut die Distanz
                        distance = calculateDistance(spherePositions[0], spherePositions[1])

                        // Aktualisiere die Linie
                        drawLineBetweenPoints(engine, nodes, materialLoader, spherePositions[0], spherePositions[1])
                    }
                }
                return@ARScene true
            }
            return@ARScene false
        },
        onSessionUpdated = { _, frame -> currentFrame = frame }
    )

    // Marker in der Mitte der Ansicht
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val circleRadius = 10f
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        drawCircle(
            color = Color.Red,
            radius = circleRadius,
            center = Offset(centerX, centerY)
        )
    }

    // Text für die angezeigte Distanz
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .offset(y = 100.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Distance: $distance",
            color = Color.White,
            style = TextStyle(fontSize = 32.sp)
        )
    }
}

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
    return (distance * 100).toFloat()
}
