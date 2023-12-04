package com.rlsreis.grit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import android.widget.TextView
import kotlin.math.log10
import kotlin.math.roundToInt


// TODO - transfer this to its own package
    class DecibelReader : AppCompatActivity() {
        private lateinit var backButton: Button
        private lateinit var decibelReaderTextView: TextView

        private var audioRecord: AudioRecord? = null
        private var isRecording = false
        private val sampleRate = 44100
        private var bufferSize = 0

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_decibel_reader)

            backButton = findViewById(R.id.back_button)
            decibelReaderTextView =
                findViewById(R.id.decibelReaderTextView)

            backButton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            // Request Permissions
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
        }

        override fun onResume() {
            super.onResume()
            startAudioRecording()
        }

        override fun onPause() {
            super.onPause()
            stopAudioRecording()
        }

        private fun startAudioRecording() {
            bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
            if (audioRecord?.state == AudioRecord.STATE_INITIALIZED) {
                audioRecord?.startRecording()
                isRecording = true
                Thread { readAudioData() }.start()
            }
        }

        private fun stopAudioRecording() {
            isRecording = false
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
        }

        private fun readAudioData() {
            val audioData = ShortArray(bufferSize)
            while (isRecording) {
                val readResult = audioRecord?.read(audioData, 0, bufferSize)
                if (readResult != null && readResult > 0) {
                    val decibels = calculateDecibels(audioData, readResult)
                    runOnUiThread {
                        // Update your UI with the decibel value
                        decibelReaderTextView.text = "Decibels: $decibels"
                    }
                }
            }
        }

        // TODO - Improve formula + add calibration
        private fun calculateDecibels(audioData: ShortArray, readSize: Int): Int {
            var sum = 0.0
            for (i in 0 until readSize) {
                sum += audioData[i] * audioData[i]
            }
            val amplitude = sum / readSize
            return (10 * log10(amplitude)).roundToInt()
        }
    }