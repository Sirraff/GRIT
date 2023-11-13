package com.rlsreis.grit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg : Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg = findViewById(R.id.register_button)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.back_login)


        textView.setOnClickListener {
            finish()
        }

        buttonReg.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Enter Email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Enter Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { emailVerificationTask ->
                                progressBar.visibility = View.GONE
                                if (emailVerificationTask.isSuccessful) {
                                    Toast.makeText(baseContext, "Verification email sent to $email.", Toast.LENGTH_SHORT).show()
                                    // TODO redirect the user to a login page or a page that instructs them to verify their email
                                } else {
                                    Toast.makeText(baseContext, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        progressBar.visibility = View.GONE
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        }
    companion object {
        private const val TAG = "RegisterActivity"
    }

    }



