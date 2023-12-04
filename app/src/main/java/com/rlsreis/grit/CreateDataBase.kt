package com.rlsreis.grit
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*




class CreateDataBase : AppCompatActivity() {
//    private lateinit var tagEditText: EditText
    private lateinit var temperatureEditText: EditText
    // private lateinit var dateEditText: EditText
    private lateinit var decibelEditText: EditText
    private lateinit var planktonConcentrationEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var backButton: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_data_base)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize views
        temperatureEditText = findViewById(R.id.temperatureEditText)
        // dateEditText = findViewById(R.id.dateEditText)

        planktonConcentrationEditText = findViewById(R.id.planktonConcentration)
        decibelEditText = findViewById(R.id.decibels)
        submitButton = findViewById(R.id.submitButton)
        backButton = findViewById(R.id.back_button)

        submitButton.setOnClickListener {
            getCurrentLocationAndTime()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    private fun getCurrentLocationAndTime() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val currentTime = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
                    val tag = "Geo_${location.latitude}_${location.longitude}_Time_$currentTime"
                    sendDataToFirestore(tag)
                }
            }
            .addOnFailureListener {e ->
                Toast.makeText(this, "Error getting location data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun sendDataToFirestore(geolocation: String) {
        val temperature = temperatureEditText.text.toString()
        val planktonConcentration = planktonConcentrationEditText.text.toString()
        val decibelReading = decibelEditText.text.toString()

        // Format the current date and time
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        // Combine geolocation and timestamp
        val documentName = "${geolocation}_$currentTime"

        // Create a new document
        val docData = hashMapOf(
            "temperature" to temperature,
            "date" to currentTime, // Use the currentTime here
            "planktonConcentration" to planktonConcentration,
            "decibelReading" to decibelReading
        )

        // Use the combined string as the document name
        db.collection("GRIT_DB").document(documentName)
            .set(docData)
            .addOnSuccessListener {
                Toast.makeText(this, "Data successfully added!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}