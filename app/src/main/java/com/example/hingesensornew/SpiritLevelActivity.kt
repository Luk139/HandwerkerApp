package com.example.hingesensornew

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpiritLevelActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var levelText: TextView
    private lateinit var horizontalIndicator: View
    private lateinit var verticalIndicator: View
    private lateinit var centralBox: View // Neues View für das zentrale Kästchen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spirit_level)

        // Initialisierung der Komponenten
        levelText = findViewById(R.id.levelText)
        horizontalIndicator = findViewById(R.id.horizontalIndicator)
        verticalIndicator = findViewById(R.id.verticalIndicator)
        //centralBox = findViewById(R.id.centralBox) // Zentrales Kästchen initialisieren

        // Initialisierung des Beschleunigungssensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            // Aktualisierung der Wasserwaage
            val x = event.values[0] // X-Achse
            val y = event.values[1] // Y-Achse
            val z = event.values[2] // Z-Achse

            // Berechnung der Neigung
            val angleX = Math.atan2(y.toDouble(), z.toDouble()) * (180 / Math.PI) // Neigung um die X-Achse (horizontal)
            val angleY = Math.atan2(x.toDouble(), z.toDouble()) * (180 / Math.PI) // Neigung um die Y-Achse (vertikal)

            // Definiere die Maximalwerte für die Wasserwaage
            val maxTranslation = 250f // Erhöhe diesen Wert für größere Bewegungen

            // Überprüfen, ob die Wasserwaage horizontal oder vertikal ist
            if (Math.abs(z) < 8) { // Schwellenwert für die Z-Achse (kann angepasst werden)
                // Vertikale Anzeige aktivieren
                horizontalIndicator.visibility = View.GONE
                verticalIndicator.visibility = View.VISIBLE

                // Berechnung der Zielposition für die vertikale Wasserwaage (Y-Achse)
                val targetVerticalY = when {
                    Math.abs(angleX - 90) < 0.01 -> 0f // In die Mitte bewegen, wenn nahe 90
                    Math.abs(angleX - 90) < 20 -> (angleX - 90) * maxTranslation / 20 // Skalierung für Werte zwischen 70 und 90
                    else -> if (angleX > 90) maxTranslation else -maxTranslation // Ganz außen
                }

                // Aktualisierung der Position des vertikalen Indikators mit Animation
                verticalIndicator.animate()
                    .translationY(targetVerticalY.toFloat())
                    .setDuration(100) // Dauer der Animation in Millisekunden
                    .start()

                // Aktualisierung des Textes
                levelText.text = "Neigung: X=${Math.round(angleX)}°, Y=${Math.round(angleY)}° (Vertikal)"
            } else {
                // Horizontale Anzeige aktivieren
                horizontalIndicator.visibility = View.VISIBLE
                verticalIndicator.visibility = View.GONE

                // Berechnung der Zielposition für die horizontale Wasserwaage (X-Achse)
                val targetHorizontalX = when {
                    Math.abs(angleY) < 0.01 -> 0f // In die Mitte bewegen, wenn nahe Null
                    Math.abs(angleY) < 20 -> angleY * maxTranslation / 20 // Skalierung für Werte zwischen 0.01 und 20
                    else -> if (angleY > 0) maxTranslation else -maxTranslation // Ganz außen
                }

                // Aktualisierung der Position des horizontalen Indikators mit Animation
                horizontalIndicator.animate()
                    .translationX(targetHorizontalX.toFloat())
                    .setDuration(250) // Dauer der Animation in Millisekunden
                    .start()

                // Aktualisierung des Textes
                levelText.text = "Neigung: X=${Math.round(angleX)}°, Y=${Math.round(angleY)}° (Horizontal)"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Nicht benötigt
    }
}