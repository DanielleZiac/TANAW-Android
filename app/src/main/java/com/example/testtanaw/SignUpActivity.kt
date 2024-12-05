package com.example.testtanaw

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var srCodeInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var institutionSpinner: Spinner
    private lateinit var institutionAdapter: InstitutionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize views
        firstNameInput = findViewById(R.id.first_name_input)
        lastNameInput = findViewById(R.id.last_name_input)
        srCodeInput = findViewById(R.id.sr_code_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
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
                Toast.makeText(this@SignUpActivity, "Selected: $selectedInstitution", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle no selection case if needed
            }
        }
    }

    // Sign-Up button click handler
    fun onSignUpClicked(view: View) {
        val firstName = firstNameInput.text.toString()
        val lastName = lastNameInput.text.toString()
        val srCode = srCodeInput.text.toString()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()
        val selectedInstitution = institutionSpinner.selectedItem.toString()

        // Validate inputs
        if (selectedInstitution == "Select Institution") {
            Toast.makeText(this, "Please select a valid institution.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && srCode.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            // Simulate sign-up success or proceed with Firebase/Auth logic
            Toast.makeText(this, "Sign-Up Successful! Welcome, $firstName!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    // Open Login Activity when the "Sign In" text is clicked
    fun openLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
