package com.example.gameland

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Registration : AppCompatActivity() {
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private var mAuth: FirebaseAuth? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewLogin: TextView

    override fun onStart() {
        super.onStart()
        mAuth?.currentUser?.let {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        mAuth = FirebaseAuth.getInstance()

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        textViewLogin = findViewById(R.id.loginNow)

        textViewLogin.setOnClickListener {
            startActivity(Intent(applicationContext, Login::class.java))
            finish()
        }

        buttonReg.setOnClickListener {
            val email = editTextEmail.text?.toString().orEmpty()
            val password = editTextPassword.text?.toString().orEmpty()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(this, "Account successfully created.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("RegistrationActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}