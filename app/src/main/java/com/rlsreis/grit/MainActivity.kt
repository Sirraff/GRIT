package com.rlsreis.grit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button
    private lateinit var oceanButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton = findViewById(R.id.logout_button)
        oceanButton = findViewById(R.id.ocean_button)

        oceanButton.setOnClickListener {
            val intent = Intent(this, OceanCam::class.java)
            startActivity(intent)
            finish()
        }
        logoutButton.setOnClickListener {
            // Log out the user
            FirebaseAuth.getInstance().signOut()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Ensure this activity is closed so the user can't navigate back to it
        }
    }
}