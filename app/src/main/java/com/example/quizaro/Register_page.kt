package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register_page : AppCompatActivity() {

    private lateinit var btnSignUp: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)

        // Apply system insets (status bar/nav bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize button
        btnSignUp = findViewById(R.id.btnSignUp)

        // On Sign Up → Redirect to MainActivity
        btnSignUp.setOnClickListener {
            val intent = Intent(this@Register_page, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: close Register page so user can't go back with Back button
        }
    }
}