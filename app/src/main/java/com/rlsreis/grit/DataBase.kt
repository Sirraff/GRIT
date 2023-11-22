package com.rlsreis.grit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore
import com.rlsreis.grit.data_models.EnvironmentalData
import java.text.SimpleDateFormat
import java.util.Locale

// TODO
// create view with search and create
// search add existing fields
// create add fields
// if creator, permission to edit???
// make sure firestore/firebase works, also see pricing :/
class DataBase : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var submitButton : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_base)

        val tagEditText: EditText = findViewById(R.id.tagEditText)
        val temperatureEditText: EditText = findViewById(R.id.temperatureEditText)
        val dateEditText: EditText = findViewById(R.id.dateEditText)
        val planktonEditText: EditText = findViewById(R.id.planktonConcentration)
// ... Define other EditText references similarly

        backButton = findViewById(R.id.back_button)
        submitButton = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val tag = tagEditText.text.toString()

            searchForTag(tag)
            val temperature = temperatureEditText.text.toString().toDoubleOrNull()
            val dateString = dateEditText.text.toString()
            val plankton = planktonEditText.text.toString().toDoubleOrNull()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)?.time ?: System.currentTimeMillis()
            // ... Gather other fields similarly

            val data = EnvironmentalData(tag, temperature, plankton, date)
            submitToFirebase(data)
        }

        backButton.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
fun searchForTag(tag: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("tags").document(tag).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                // Tag exists, show options to view or contribute to this tag
            } else {
                // Tag does not exist, prompt to create new or search again
            }
        }
        .addOnFailureListener { e ->
            // Handle errors, e.g., show an error message
        }
}

private fun submitToFirebase(data: EnvironmentalData) {
    val db = FirebaseFirestore.getInstance()
    db.collection("tags").document(data.tag)
        .collection("entries").add(data)
        .addOnSuccessListener {
            // Handle success, maybe show a Toast or navigate away
        }
        .addOnFailureListener {
            // Handle error, show an error message
        }
}