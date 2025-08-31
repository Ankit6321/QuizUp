package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizaro.network.LoginRequest
import com.example.quizaro.network.RetrofitClient
import com.example.quizaro.network.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var btnlogin: AppCompatButton
    private lateinit var registerText: TextView
    private lateinit var forgotPasswordText: TextView
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize Views
        btnlogin = findViewById(R.id.btnlogin)
        registerText = findViewById(R.id.register)
        forgotPasswordText = findViewById(R.id.forgotpassword)
        usernameInput = findViewById(R.id.username)
        passwordInput = findViewById(R.id.password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Login button -> Call API
        btnlogin.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username & password", Toast.LENGTH_SHORT).show()
            } else {
                doLogin(username, password)
            }
        }

        // 🔹 Register text -> Register Page
        registerText.setOnClickListener {
            val intent = Intent(this@MainActivity, Register_page::class.java)
            startActivity(intent)
        }

        // 🔹 Forgot Password text -> Forgot Password Page
        forgotPasswordText.setOnClickListener {
            val intent = Intent(this@MainActivity, Forgot_password_page::class.java)
            startActivity(intent)
        }
    }

    // ✅ Retrofit Login Function
    private fun doLogin(username: String, password: String) {
        val request = LoginRequest(username, password)
        val call = RetrofitClient.api.loginUser(request)

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val userResponse = response.body()!!
                    Toast.makeText(
                        this@MainActivity,
                        "Login Success! Welcome ${userResponse.user}",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 🔹 Navigate to Home Page
                    val intent = Intent(this@MainActivity, Home_page::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Login Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", "Login failed", t)
            }
        })
    }
}
