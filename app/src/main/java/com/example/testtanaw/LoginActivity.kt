package com.example.testtanaw

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var srCodeInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var institutionSpinner: Spinner
    private lateinit var institutionAdapter: InstitutionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        srCodeInput = findViewById(R.id.sr_code_input)
        passwordInput = findViewById(R.id.password_input)
        institutionSpinner = findViewById(R.id.institution_spinner)

        // Set up spinner adapter with custom adapter
        val institutions = resources.getStringArray(R.array.institutions).toList()
        institutionAdapter = InstitutionAdapter(this, institutions)
        institutionSpinner.adapter = institutionAdapter

        // Ensure the spinner has a valid selection by setting a default item
        institutionSpinner.setSelection(0)

        // Handle spinner item selection
        institutionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedInstitution = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@LoginActivity, "Selected: $selectedInstitution", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle no selection case if needed
            }
        }
    }

    // Login button click handler
    fun onLoginClicked(view: View) {
        val srCode = srCodeInput.text.toString()
        val password = passwordInput.text.toString()
        val selectedInstitution = institutionSpinner.selectedItem.toString()

        // Validate institution selection
        if (selectedInstitution == "Select Institution") {
            Toast.makeText(this, "Please select a valid institution.", Toast.LENGTH_SHORT).show()
            return
        }

        // Simple validation
        if (srCode.isNotEmpty() && password.isNotEmpty()) {
            // You can replace this with actual login logic
            val intent = Intent(this, MainActivity::class.java)
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
