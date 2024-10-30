package com.example.hingesensornew

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor
    private lateinit var tvCurrentAngle: TextView
    private lateinit var btnCalibrate: Button
    private lateinit var tvCountdown: TextView

    // Gyroscope calibration values
    private var calGyroX = 0.0
    private var calGyroY = 0.0
    private var calGyroZ = 0.0

    // Angle value
    private var currentAngle = 0 // Start bei 180°

    // Countdown timer
    private var countdownTimer: CountDownTimer? = null

    // Flag to check if calibration is in progress
    private var isCalibrating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentAngle = findViewById(R.id.tvCurrentAngle)
        btnCalibrate = findViewById(R.id.btnCalibrate)
        tvCountdown = findViewById(R.id.tvCountdown)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        btnCalibrate.setOnClickListener {
            startCalibration()
        }

        val btnOpenCamera: Button = findViewById(R.id.btnOpenCamera)
        btnOpenCamera.setOnClickListener {
            // Wechselt zur CameraActivity
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        val btnOpenSpiritLevel: Button = findViewById(R.id.btnOpenSpiritLevel)
        btnOpenSpiritLevel.setOnClickListener {
            // Wechselt zur CameraActivity
            val intent = Intent(this, SpiritLevelActivity::class.java)
            startActivity(intent)
        }

        startCalibration()
    }

    private fun startCalibration() {
        // Start countdown for calibration
        tvCountdown.visibility = TextView.VISIBLE
        countdownTimer?.cancel() // Cancel any existing timer

        isCalibrating = true // Set calibration flag

        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvCountdown.text = "Countdown: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                tvCountdown.text = "Kalibrierung abgeschlossen"
                tvCountdown.visibility = TextView.GONE
                calibrateGyroscope()
                isCalibrating = false // Reset calibration flag
            }
        }.start()
    }

    private fun calibrateGyroscope() {
        // Setze die Kalibrierungswerte auf 0
        calGyroX = 0.0
        calGyroY = 0.0
        calGyroZ = 0.0
        currentAngle = 0 // Nach der Kalibrierung auf 180 setzen
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            // Ignore sensor updates if calibrating
            if (isCalibrating) return

            val gyroY = event.values[1] - calGyroY // Nehmen Sie den Y-Wert für die Berechnung

            // Berechnung des Winkels
            // Reduzieren Sie den aktuellen Winkel basierend auf der Gyroskopbewegung
            currentAngle -= (gyroY * 13.5).toInt() // Multiplikation für bessere Sensitivität
            currentAngle = currentAngle.coerceIn(0, 180) // Beschränkung zwischen 0 und 180

            // Aktualisieren Sie die TextView mit dem aktuellen Winkel
            val tmpangel = 180 - currentAngle
            tvCurrentAngle.text = "Aktueller Winkel: $tmpangel°"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Nicht benötigt
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}