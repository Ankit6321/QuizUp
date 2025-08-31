package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizaro.network.RegisterRequest
import com.example.quizaro.network.RetrofitClient
import com.example.quizaro.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register_page : AppCompatActivity() {

    private lateinit var btnSignUp: AppCompatButton
    private lateinit var usernameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)

        // Apply system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        usernameInput = findViewById(R.id.username)
        emailInput = findViewById(R.id.email)
        passwordInput = findViewById(R.id.password)
        btnSignUp = findViewById(R.id.btnSignUp)

        // Sign Up button click → call API
        btnSignUp.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                doRegister(username, email, password)
            }
        }
    }

    // ✅ Retrofit registration function
    private fun doRegister(username: String, email: String, password: String) {
        val request = RegisterRequest(username, email, password)
        val call = RetrofitClient.api.registerUser(request)

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    Toast.makeText(
                        this@Register_page,
                        "Registration Successful! Welcome ${userResponse.user.username}",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Navigate to SignIn page
                    val intent = Intent(this@Register_page, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@Register_page,
                        "Registration failed: ${response.code()} ${response.errorBody()?.string()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(
                    this@Register_page,
                    "Registration Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
