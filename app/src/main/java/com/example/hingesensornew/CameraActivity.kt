package com.example.hingesensornew

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.google.android.filament.Engine
import com.google.ar.core.Config
import com.google.ar.core.Frame
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.hitTest
import io.github.sceneview.collision.HitResult
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.node.SphereNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMainLightNode
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

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
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)
    val collisionSystem = rememberCollisionSystem(view)
    val cameraNode = rememberCameraNode(engine)

    val nodes = rememberNodes()

    var currentFrame by remember { mutableStateOf<Frame?>(null) }

    val spherePositions = remember { mutableListOf<Position>() }

    var distance by remember { mutableStateOf("- cm") }

    var cubeNode: Node? by remember { mutableStateOf(null) }

    val cubeNodes = remember { mutableListOf<Node>() }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        engine = engine,
        view = view,
        renderer = renderer,
        scene = scene,
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        environmentLoader = environmentLoader,
        collisionSystem = collisionSystem,
        isOpaque = true,
        planeRenderer = false,
        mainLightNode = rememberMainLightNode(engine) {
            intensity = 100_000.0f
            transform(
                position = Position(0f, 100f, 0f),
                rotation = Rotation(x = 0f, y = 0f, z = 0f)
            )
        },
        sessionConfiguration = { session, config ->
            config.depthMode = if (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                Config.DepthMode.AUTOMATIC
            } else {
                Config.DepthMode.DISABLED
            }
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
                            transform(
                                position = positionInFrontOfCamera,
                                rotation = Rotation(x = 0.0f)
                            )
                        }
                        nodes.add(newSphere)
                        spherePositions.add(positionInFrontOfCamera)

                        if (spherePositions.size == 2) {
                            distance = calculateDistance(spherePositions[0], spherePositions[1])
                            cubeNodes.forEach { nodes.remove(it) }
                            cubeNodes.clear()

                            placeSpheresAlongPath(
                                engine = engine,
                                nodes = nodes,
                                materialLoader = materialLoader,
                                startPosition = spherePositions[0],
                                endPosition = spherePositions[1],
                                cubeNodes = cubeNodes
                            )
                        }
                    } else {
                        val firstSphere = nodes.firstOrNull() as? SphereNode
                        firstSphere?.let {
                            nodes.remove(it)
                        }
                        spherePositions.removeAt(0)

                        spherePositions.add(positionInFrontOfCamera)

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
                            transform(
                                position = positionInFrontOfCamera,
                                rotation = Rotation(x = 0.0f)
                            )
                        }
                        nodes.add(newSphere)

                        if (spherePositions.size == 2) {
                            distance = calculateDistance(spherePositions[0], spherePositions[1])
                            cubeNodes.forEach { nodes.remove(it) }
                            cubeNodes.clear()

                            placeSpheresAlongPath(
                                engine = engine,
                                nodes = nodes,
                                materialLoader = materialLoader,
                                startPosition = spherePositions[0],
                                endPosition = spherePositions[1],
                                cubeNodes = cubeNodes
                            )
                        }
                    }
                }
                return@ARScene true
            }
            return@ARScene false
        },
        onSessionUpdated = { _, frame ->
            currentFrame = frame
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = {},
            content = { Text("Add Point") }
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val circleRadius = 10f
        val centerX = canvasWidth / 2
        val centerY = canvasHeight / 2

        drawCircle(
            color = Color.White,
            radius = circleRadius,
            center = Offset(centerX, centerY)
        )
    }

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

fun placeSpheresAlongPath(
    engine: Engine,
    nodes: MutableList<Node>,
    materialLoader: MaterialLoader,
    startPosition: Position,
    endPosition: Position,
    cubeNodes: MutableList<Node>
) {
    // Define the number of spheres we want to place along the path
    val numberOfSpheres = 10

    // Calculate the increments for each axis to place the spheres evenly along the path
    val dx = (endPosition.x - startPosition.x) / (numberOfSpheres - 1)
    val dy = (endPosition.y - startPosition.y) / (numberOfSpheres - 1)
    val dz = (endPosition.z - startPosition.z) / (numberOfSpheres - 1)

    // Clear any previously placed spheres along the path
    cubeNodes.forEach { nodes.remove(it) }
    cubeNodes.clear()

    // Place exactly 10 spheres along the path
    for (i in 0 until numberOfSpheres) {
        val spherePosition = Position(
            x = startPosition.x + dx * i,
            y = startPosition.y + dy * i,
            z = startPosition.z + dz * i
        )
        val sphereNode = SphereNode(
            engine = engine,
            radius = 0.005f,
            materialInstance = materialLoader.createColorInstance(
                color = Color.Yellow,
                metallic = 0.0f,
                roughness = 0.0f,
                reflectance = 0.0f
            )
        ).apply {
            transform(position = spherePosition)
        }
        nodes.add(sphereNode)
        cubeNodes.add(sphereNode)
    }
}


fun calculateDistance(position1: Position, position2: Position): String {
    val dx = position2.x - position1.x
    val dy = position2.y - position1.y
    val dz = position2.z - position1.z
    val distance = Math.sqrt((dx * dx + dy * dy + dz * dz).toDouble())
    return String.format("%.2f cm", distance * 100)
}
