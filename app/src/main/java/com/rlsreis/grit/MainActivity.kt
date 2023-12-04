package com.rlsreis.grit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var logoutButton: Button // only get initialized on call(hence late init)
    private lateinit var toolsButton: Button
    private lateinit var dbButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolsButton = findViewById(R.id.tools_button)
        logoutButton = findViewById(R.id.logout_button)
        dbButton = findViewById(R.id.db_button)


        logoutButton.setOnClickListener {
            // Log out the user
            FirebaseAuth.getInstance().signOut()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Ensure this activity is closed so the user can't navigate back to it
        }

        toolsButton.setOnClickListener {
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
            finish()
        }

        dbButton.setOnClickListener {
            val intent = Intent(this, CreateDataBase::class.java)
            startActivity(intent)
            finish()
        }


    }
}