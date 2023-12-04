package com.rlsreis.grit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ToolsActivity : AppCompatActivity() {
    private lateinit var backButton: Button
    private  lateinit var planktonButton: Button
    private lateinit var deciButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)

        backButton = findViewById(R.id.back_button)
        planktonButton = findViewById(R.id.ocean_button)
        deciButton = findViewById(R.id.decibel_button)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        planktonButton.setOnClickListener {
            val intent = Intent(this, OceanCam::class.java)
            startActivity(intent)
            finish()
        }

        deciButton.setOnClickListener {
            val intent = Intent(this, DecibelReader::class.java)
            startActivity(intent)
            finish()
        }
    }
}