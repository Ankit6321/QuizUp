package com.example.quizaro

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizaro.network.ApiService
import com.example.quizaro.network.ResetConfirmRequest
import com.example.quizaro.network.ResetRequest
import com.example.quizaro.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Forgot_password_page : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private lateinit var btnCheck: AppCompatButton
    private lateinit var btnResetPassword: AppCompatButton

    // To store uid & token returned from backend
    private var uid: String? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.forgot_password)

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize fields
        etUsername = findViewById(R.id.username)
        etEmail = findViewById(R.id.etEmail)
        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        btnCheck = findViewById(R.id.btnCheck)
        btnResetPassword = findViewById(R.id.btnResetPassword)

        btnCheck.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            if(username.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Enter both username & email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            requestPasswordReset(username, email)
        }

        btnResetPassword.setOnClickListener {
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if(newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Enter both password fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            confirmPasswordReset(newPassword, confirmPassword)
        }
    }

    private fun requestPasswordReset(username: String, email: String) {
        val request = ResetRequest(username, email)
        val call = RetrofitClient.api.requestPasswordReset(request)

        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if(response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    uid = data["uid"] as? String
                    token = data["token"] as? String

                    if(uid != null && token != null) {
                        Toast.makeText(this@Forgot_password_page, "User matched! Enter new password", Toast.LENGTH_SHORT).show()
                        // Show new password fields
                        etNewPassword.visibility = View.VISIBLE
                        etConfirmPassword.visibility = View.VISIBLE
                        btnResetPassword.visibility = View.VISIBLE
                    } else {
                        Toast.makeText(this@Forgot_password_page, "No matching user found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Forgot_password_page, "Check failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@Forgot_password_page, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun confirmPasswordReset(newPassword: String, confirmPassword: String) {
        if(uid == null || token == null) return

        val request = ResetConfirmRequest(newPassword, confirmPassword)
        val call = RetrofitClient.api.confirmPasswordReset(uid!!, token!!, request)

        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if(response.isSuccessful) {
                    Toast.makeText(this@Forgot_password_page, "Password reset successfully!", Toast.LENGTH_SHORT).show()
                    finish() // go back to login page
                } else {
                    Toast.makeText(this@Forgot_password_page, "Reset failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@Forgot_password_page, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
