package com.example.hingesensornew

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Point
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableException
import android.view.View

class CameraActivity : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var btnPlacePoint: Button
    private lateinit var tvDistance: TextView
    private lateinit var marker: View

    private var session: Session? = null
    private var firstPointAnchor: Anchor? = null
    private var secondPointAnchor: Anchor? = null

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            startARSession()
        } else {
            tvDistance.text = "Kamera-Berechtigung nicht gewährt"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        previewView = findViewById(R.id.previewView)
        btnPlacePoint = findViewById(R.id.btnPlacePoint)
        tvDistance = findViewById(R.id.tvDistance)
        marker = findViewById(R.id.marker)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startARSession()
        } else {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }

        btnPlacePoint.setOnClickListener {
            placePoint()
        }
    }

    private fun isARCoreSupported(): Boolean {
        return try {
            Session(this)
            true
        } catch (e: UnavailableException) {
            Log.e("CameraActivity", "ARCore nicht unterstützt: ${e.message}")
            false
        }
    }

    private fun startARSession() {
        if (!isARCoreSupported()) {
            tvDistance.text = "ARCore wird auf diesem Gerät nicht unterstützt."
            return
        }

        if (session == null) {
            try {
                session = Session(this) // Sitzungsobjekt nur einmal erstellen
                session?.resume() // Sitzung fortsetzen
                startCamera() // Kamera erst nach erfolgreicher Sitzungsinitialisierung starten
            } catch (e: UnavailableException) {
                Log.e("CameraActivity", "ARCore-Sitzung nicht verfügbar: ${e.message}")
            }
        } else {
            session?.resume() // Falls die Sitzung bereits existiert, fortsetzen
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().apply {
                    setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll() // Verhindert doppeltes Binden
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (e: Exception) {
                Log.e("CameraActivity", "Fehler beim Starten der Kamera: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun placePoint() {
        if (session == null) {
            Log.e("CameraActivity", "Sitzung ist null")
            return
        }

        try {
            val frame = session!!.update() // Frame aktualisieren
            val hitResult: HitResult? = frame.hitTest(previewView.width / 2f, previewView.height / 2f)?.firstOrNull()

            if (hitResult != null && hitResult.trackable is Point) {
                val newAnchor = hitResult.createAnchor()

                if (firstPointAnchor == null) {
                    firstPointAnchor = newAnchor
                    tvDistance.text = "Erster Punkt gesetzt"
                    marker.visibility = View.GONE // Marker ausblenden
                } else if (secondPointAnchor == null) {
                    secondPointAnchor = newAnchor
                    calculateDistance()
                }
            } else {
                Log.e("CameraActivity", "Hit result ist null oder kein Punkt")
            }
        } catch (e: Exception) {
            Log.e("CameraActivity", "Fehler beim Aktualisieren der Sitzung: ${e.message}")
        }
    }

    private fun calculateDistance() {
        if (firstPointAnchor == null || secondPointAnchor == null) return

        val firstTranslation = firstPointAnchor!!.pose.translation
        val secondTranslation = secondPointAnchor!!.pose.translation

        val dx = firstTranslation[0] - secondTranslation[0]
        val dy = firstTranslation[1] - secondTranslation[1]
        val dz = firstTranslation[2] - secondTranslation[2]

        val distanceMeters = Math.sqrt((dx * dx + dy * dy + dz * dz).toDouble())
        tvDistance.text = "Entfernung: %.2f m".format(distanceMeters)
    }

    override fun onPause() {
        super.onPause()
        Log.d("CameraActivity", "Pausing AR session")
        session?.pause() // Sitzung pausieren
    }

    override fun onResume() {
        super.onResume()
        Log.d("CameraActivity", "Resuming AR session")

        // Falls Kamera-Berechtigung erteilt, die Sitzung nur dann fortsetzen, wenn sie aktiv ist
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            session?.resume() // Sitzung fortsetzen, falls sie existiert
        }
    }
}
