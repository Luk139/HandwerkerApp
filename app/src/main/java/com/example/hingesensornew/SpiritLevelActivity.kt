package com.example.hingesensornew

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue

class SpiritLevelActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = SpiritLevelViewModel(this)

        setContent {
            val x by viewModel.x
            val y by viewModel.y
            val angleX by viewModel.angleX
            val angleY by viewModel.angleY
            val isVertical by viewModel.isVertical

            SpiritLevelScreen(
                x = x,
                y = y,
                angleX = angleX,
                angleY = angleY,
                isVertical = isVertical
            )
        }
    }
}
