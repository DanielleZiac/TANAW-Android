package com.example.testtanaw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testtanaw.util.DB
import com.example.testtanaw.util.InstitutionAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var firstNameInput: EditText
    private lateinit var lastNameInput: EditText
    private lateinit var srCodeInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var institutionSpinner: Spinner
    private lateinit var institutions: List<DB.Institution>
    private val db = DB()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        CoroutineScope(Dispatchers.Main).launch {
            srCodeInput = findViewById(R.id.sr_code_input)
            firstNameInput = findViewById(R.id.first_name_input)
            lastNameInput = findViewById(R.id.last_name_input)
            institutionSpinner = findViewById(R.id.institution_spinner)
            passwordInput = findViewById(R.id.password_input)
            confirmPasswordInput = findViewById(R.id.confirm_password_input)

            institutions = db.getInstitutions(this@SignUpActivity, institutionSpinner)

            // Handle spinner item selection
            institutionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedInstitution = parent?.getItemAtPosition(position).toString()
                    Toast.makeText(
                        this@SignUpActivity,
                        "Selected: $selectedInstitution",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle no selection case if needed
                }
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
        val selectedInstitution = institutions[institutionSpinner.selectedItemPosition].institution
        val emailExtension: String = institutions[institutionSpinner.selectedItemPosition].emailExtension
        val institutionId: String = institutions[institutionSpinner.selectedItemPosition].institutionId

        // Validate inputs
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("xxxxxx", "$selectedInstitution, $emailExtension")

        CoroutineScope(Dispatchers.Main).launch {
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && srCode.isNotEmpty() && password.isNotEmpty() && emailExtension.isNotEmpty()) {
                val user = db.signup(firstName, lastName, srCode, password, emailExtension, institutionId)

                if (user != null) {
                    Log.d("xxxxxx", user.toString())

                    Toast.makeText(this@SignUpActivity, "check your email", Toast.LENGTH_SHORT).show()
                    firstNameInput.text.clear()
                    lastNameInput.text.clear()
                    srCodeInput.text.clear()
                    passwordInput.text.clear()
                    confirmPasswordInput.text.clear()
                    institutionSpinner.setSelection(0)

                } else {
                    Log.d("xxxxxx", "Failed to sign up user. Response is null.")
                }

            } else {
                Toast.makeText(this@SignUpActivity, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Open Login Activity when the "Sign In" text is clicked
    fun openLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
