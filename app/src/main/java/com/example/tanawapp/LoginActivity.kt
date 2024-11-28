package com.example.tanawapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var srCodeInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        srCodeInput = findViewById(R.id.sr_code_input)
        passwordInput = findViewById(R.id.password_input)
    }

    // Login button click handler
    fun onLoginClicked(view: View) {
        val srCode = srCodeInput.text.toString()
        val password = passwordInput.text.toString()

        // Simple validation
        if (srCode.isNotEmpty() && password.isNotEmpty()) {
            // You can replace this with actual login logic
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
        }
    }

    // Open SignUp Activity when the "Sign Up" text is clicked
    fun openSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
