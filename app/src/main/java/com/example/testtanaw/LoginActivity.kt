package com.example.testtanaw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.testtanaw.models.UserParcelable
import com.example.testtanaw.util.DB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val DB = DB()
    private lateinit var srCodeInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var institutionSpinner: Spinner
    private lateinit var institutions: List<DB.Institution>
    private lateinit var institutionAdapter: InstitutionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        CoroutineScope(Dispatchers.Main).launch {
            // Initialize views
            srCodeInput = findViewById(R.id.sr_code_input)
            passwordInput = findViewById(R.id.password_input)
            institutionSpinner = findViewById(R.id.institution_spinner)

            institutions = DB.getInstitutions(this@LoginActivity)

            // Create a list of institution names to display in the Spinner
            val institutionNames = institutions.map { it.institution }.toMutableList()
            institutionAdapter = InstitutionAdapter(this@LoginActivity, institutionNames)
            institutionSpinner.adapter = institutionAdapter

            // Ensure the spinner has a valid selection by setting a default item
            institutionSpinner.setSelection(0)
        }
    }

    // Login button click handler
    fun onLoginClicked (view: View) {
        val srCode = srCodeInput.text.toString()
        val password = passwordInput.text.toString()
        val selectedInstitution = institutions[institutionSpinner.selectedItemPosition].institution
        val emailExtension: String = institutions[institutionSpinner.selectedItemPosition].emailExtension

        Log.d("xxxxxx", "$selectedInstitution, $emailExtension")
        // Validate institution selection
        if (selectedInstitution == "Select Institution") {
            Toast.makeText(this, "Please select a valid institution.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            if (srCode.isNotEmpty() && password.isNotEmpty()) {
                try {
                    val email = srCode + emailExtension
                    Log.d("xxxxxx", "email: $email")

                    val userId = DB.login(email, password, this@LoginActivity)
                    if (userId != null) {
                        // redirect ??

                        // check if may avatar -- if wala, redirect sa avatar creation dapat??
                        val userDbData: DB.UserData? = DB.getUserDataByUserId(userId);
                        Log.d("xxxxxx", "avatarrr: {${userDbData?.avatars?.get("avatar_url")}}")

                        if (userDbData != null) {
                            if (userDbData.avatars?.get("avatar_url")?.isEmpty() == true ||
                                (userDbData.departments?.get("department")?.isEmpty() == true)) {
                                // redirect sa create avatar???

//                            var userId: String,
//                            var email: String,
//                            var srCode: String,
//                            var firstName: String,
//                            var lastName: String,
//                            var institution: String,
//                            var institutionLogo: String,
//                            var campus: String,
//                            var department: String? = null,
//                            var avatarUrl: String? = null
                            } else {
                                val userData = UserParcelable(
                                    userDbData.userId,
                                    userDbData.email,
                                    userDbData.srCode,
                                    userDbData.firstName,
                                    userDbData.lastName,
                                    userDbData.institutions["institution"]!!,
                                    userDbData.institutions["institution_logo"]!!,
                                    userDbData.institutions["campus"]!!,
                                    userDbData.departments?.get("department"),
                                    userDbData.avatars?.get("avatar_url")
                                )

                                val intent = Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                                intent.putExtra("userData", userData)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error signing up: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // Open SignUp Activity when the "Sign Up" text is clicked
    fun openSignUp(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
