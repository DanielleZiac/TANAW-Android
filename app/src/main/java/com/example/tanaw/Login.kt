package com.example.tanaw

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("sr_code") val sr_code: String,
    @SerialName("email") val email: String?,
    @SerialName("first_name") val first_name: String,
    @SerialName("last_name") val last_name: String,
    @SerialName("department") val department: String
)



class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        CoroutineScope(Dispatchers.Main).launch {
            val srCodeText = findViewById<EditText>(R.id.srCode)
            val emailExtensionText = findViewById<TextView>(R.id.emailExtension)
            val institutionSpinner = findViewById<Spinner>(R.id.institution)
            val passwordText = findViewById<EditText>(R.id.password)
            val errorText = findViewById<TextView>(R.id.error)
            val loginBtn = findViewById<Button>(R.id.loginBtn)

            val institutions = fetchAndDisplayInstitutions(institutionSpinner, errorText, this@Login)


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

            loginBtn.setOnClickListener(object: View.OnClickListener {
                override fun onClick(view: View) {

                    val srCode = srCodeText.text.toString();
                    val institution = institutionSpinner.selectedItem.toString();
                    val password = passwordText.text.toString();

                    if (srCode.isNotEmpty() && institution.isNotEmpty() && password.isNotEmpty()) {
                        Log.d("tag", srCode)
                        Log.d("tag", institution)
                        Log.d("tag", password)

                        login(srCode, institution, password, emailExtensionText.text.toString(), errorText);
                    } else {
                        Toast.makeText(this@Login, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
//                        errorText.text = "all fields are required"
                    }
                }
            })
        }
    }

    private fun login(srCode: String, institution: String, passwordd: String, emailExtension: String, errorText: TextView) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val emaill = srCode + emailExtension

                val user = supabase.auth.signInWith(Email) {
                    email=emaill
                    password=passwordd
                }

                if (user != null) {
                    Log.d("tag", user.toString())
                    errorText.text = "logged in!"

                    // redirect ??
                    val i = Intent(
                        this@Login,
                        Home::class.java
                    )
                    val user_id = getUserId();
                    i.putExtra("user_id", user_id)
                    startActivity(i)

                } else {
                    errorText.text = "Failed to login up user. Response is null."
                }
            } catch (e: Exception) {
                errorText.text = "error login ${e.message}"
            }
        }
    }
}



public suspend fun getUserId(): String {
    return try {
        val user = supabase.auth.currentUserOrNull()

        if (user != null) {
            Log.d("tag", "${user.id}")

            // upsert data to db
            val user_data = supabase
                .from("users")
                .select(columns = Columns.list("user_id")){
                    filter { eq("user_id", user.id)} }
                .decodeList<String>()

            if (user_data.isEmpty()) {
                Log.d("tag", "${user.toString()}")
                val add_user = UserData(sr_code = user.identities?.firstOrNull()?.identityData?.get("srCode").toString(),
                    email = user.email,
                    first_name = user.identities?.firstOrNull()?.identityData?.get("firstName").toString(),
                    last_name  = user.identities?.firstOrNull()?.identityData?.get("lastName").toString(),
                    department = "temp")
                supabase.from("users").upsert(add_user)
            } else {
                Log.d("tag", "user_data: ${user_data.toString()}")
            }


            user.id
        } else {
            Log.d("tag", "no user found")

            ""
        }
    } catch (e: Exception) {
        Log.d("tag", "error getUser ${e.message}")
        ""
    }
}