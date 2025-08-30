package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    private lateinit var btnlogin: AppCompatButton
    private lateinit var registerText: TextView
    private lateinit var forgotPasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        btnlogin = findViewById(R.id.btnlogin)
        registerText = findViewById(R.id.register)
        forgotPasswordText = findViewById(R.id.forgotpassword)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Login button -> Home Page
        btnlogin.setOnClickListener {
            val intent = Intent(this@MainActivity, Home_page::class.java)
            startActivity(intent)
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
}