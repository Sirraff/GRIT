package com.rlsreis.grit
import analyzers.OceanAnalyzer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

class OceanCam : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocean_cam)

        val previewView = findViewById<PreviewView>(R.id.previewView)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this), OceanAnalyzer { color ->
                        updateColorDisplay(color)
                    })
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this as LifecycleOwner, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                // Handle exceptions
            }

        }, ContextCompat.getMainExecutor(this))

        val exitButton = findViewById<Button>(R.id.exitButton)
        exitButton.setOnClickListener {
            finish()
        }
    }

    private fun updateColorDisplay(medianColor: Int) {
        val colorHex = String.format("#%06X", 0xFFFFFF and medianColor)
        runOnUiThread {
            findViewById<TextView>(R.id.colorValueTextView).text = colorHex
        }
    }
}
