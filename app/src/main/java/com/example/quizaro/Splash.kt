package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {

    private val splashDuration: Long = 2500 // 2.5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Animate logo and text
        val logo = findViewById<ImageView>(R.id.splash_logo)
        val text = findViewById<TextView>(R.id.splash_text)
        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation)
        logo.startAnimation(animation)
        text.startAnimation(animation)

        // Navigate to SignIn page after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish() // close splash
        }, splashDuration)
    }
}
