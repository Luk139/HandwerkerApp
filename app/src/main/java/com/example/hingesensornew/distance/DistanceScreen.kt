package com.example.hingesensornew.distance

import android.view.MotionEvent
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ar.core.Config
import com.google.ar.core.Frame
import io.github.sceneview.ar.ARScene
import io.github.sceneview.math.Position
import io.github.sceneview.node.SphereNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

@Composable
fun DistanceScreen(distanceScreenViewModel: DistanceScreenViewModel, onClick: () -> Unit) {
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
                            distance = distanceScreenViewModel.calculateDistance(spherePositions[0], spherePositions[1])

                            distanceScreenViewModel.drawLineBetweenPoints(engine, nodes, materialLoader, spherePositions[0], spherePositions[1])
                        }
                    } else {
                        spherePositions[0] = spherePositions[1]
                        spherePositions[1] = positionInFrontOfCamera
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
                        nodes.add(1, newSphere)

                        distance = distanceScreenViewModel.calculateDistance(spherePositions[0], spherePositions[1])


                        nodes.removeRange(2, nodes.size)

                        distanceScreenViewModel.drawLineBetweenPoints(engine, nodes, materialLoader, spherePositions[0], spherePositions[1])
                    }
                }
                return@ARScene true
            }
            return@ARScene false
        },
        onSessionUpdated = { _, frame -> currentFrame = frame }
    )

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .offset(y = 100.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            text = "Distanz: $distance",
            color = Color.White,
            style = TextStyle(fontSize = 32.sp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .offset(y = 0.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (distance != 0f) {
            Button(
                onClick = {
                    onClick()
                },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Speicher Distanz",
                    color = Color.Black,
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}





