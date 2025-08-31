package com.example.quizaro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizaro.network.ResetRequest
import com.example.quizaro.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Forgot_password_page : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var btnCheck: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.forgot_password)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgot_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailInput = findViewById(R.id.etEmail)
        btnCheck = findViewById(R.id.btnCheck)

        btnCheck.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                requestReset(email)
            }
        }
    }

    private fun requestReset(email: String) {
        val request = ResetRequest(email)
        val call = RetrofitClient.api.requestPasswordReset(request)

        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Forgot_password_page, "Reset link sent! Check your email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@Forgot_password_page, "Failed to send reset link", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@Forgot_password_page, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
