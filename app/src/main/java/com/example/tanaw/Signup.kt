package com.example.tanaw

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


val supabase = createSupabaseClient(
    supabaseUrl = BuildConfig.SUPABASE_URL,
    supabaseKey = BuildConfig.SUPABASE_ANON_KEY
) {
    install(Auth)
    install(Postgrest)
    install(Realtime)
    //install other modules
}


@Serializable
data class Institution(
    @SerialName("institution") val institution: String,
    @SerialName("email_extension") val email_extension: String
)


class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        CoroutineScope(Dispatchers.Main).launch {
            val srCodeText = findViewById<EditText>(R.id.srCode)
            val emailExtensionText = findViewById<TextView>(R.id.emailExtension)
            val firstNameText = findViewById<EditText>(R.id.firstName)
            val lastNameText = findViewById<EditText>(R.id.lastName)
            val institutionSpinner = findViewById<Spinner>(R.id.institution)
            val passwordText = findViewById<EditText>(R.id.password)
            val confirmPasswordText = findViewById<EditText>(R.id.confirmPassword)
            val errorText = findViewById<TextView>(R.id.error)
            val signupBtn = findViewById<Button>(R.id.signupBtn)

            val institutions = fetchAndDisplayInstitutions(institutionSpinner, errorText, this@Signup)

            institutionSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                    Log.d("tag", institutionSpinner.selectedItem.toString())
//                    Log.d("tag", institutionSpinner.selectedItemPosition.toString())
                    val emailExtension = institutions[institutionSpinner.selectedItemPosition].email_extension.toString()
                    emailExtensionText.text = emailExtension
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }

            signupBtn.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View) {
                    Log.d("tag", "heree")

                    val srCode = srCodeText.text.toString();
                    val firstName = firstNameText.text.toString();
                    val lastName = lastNameText.text.toString();
                    val institution = institutionSpinner.selectedItem.toString();
                    val password = passwordText.text.toString();
                    val confirmPassword = confirmPasswordText.text.toString();

                    Log.d("tag", srCode)
                    Log.d("tag", firstName)
                    Log.d("tag", lastName)
                    Log.d("tag", institution)
                    Log.d("tag", password)
                    Log.d("tag", confirmPassword)

                    if (password != confirmPassword) {
                        errorText.text = "password != confirm password"
                    } else {
                        signup(srCode, firstName, lastName, institution, password, emailExtensionText.text.toString(), errorText);
                    }
                }
            })
        }
    }

    private fun signup(srCode: String, firstName: String, lastName: String, institution: String, passwordd: String, emailExtension: String, errorText: TextView) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val emaill = srCode + emailExtension
                Log.d("tag", emaill)

                val user = supabase.auth.signUpWith(Email) {
                    email=emaill
                    password=passwordd
                    data = buildJsonObject {
                        put("srCode", srCode)
                        put("firstName", firstName)
                        put("lastName", lastName)
                        put("school", institution)
                    }
                }

                if (user != null) {
                    Log.d("tag", user.toString())
                    errorText.text = "check your email"
                } else {
                    errorText.text = "Failed to sign up user. Response is null."
                }
            } catch (e: Exception) {
                errorText.text = "error signup ${e.message}"
            }
        }
    }
}

public suspend fun fetchAndDisplayInstitutions(institutionSpinner: Spinner, errorText: TextView, appComp: AppCompatActivity): List<Institution> {
    return try {
        val results = withContext(Dispatchers.IO) {
            supabase
                .from("institutions")
                .select(columns = Columns.list("institution", "email_extension"))
                .decodeList<Institution>()
        }

        // Create a list of institution names to display in the Spinner
        val institutionNames = results.map { it.institution }

        // Create an ArrayAdapter to bind the list to the Spinner
        val adapter = ArrayAdapter(
            appComp,
            android.R.layout.simple_spinner_item,
            institutionNames
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Set the adapter for the Spinner
        institutionSpinner.adapter = adapter

        results

    } catch (e: Exception) {
        errorText.text = "Error fetching data: ${e.message}"
        emptyList()
    }
}