package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Ranking_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ranking_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ranking_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Navigation setup
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navRanking = findViewById<LinearLayout>(R.id.navRanking)
        val navStats = findViewById<LinearLayout>(R.id.navStats)

        navHome.setOnClickListener {
            val intent = Intent(this, Home_page::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0) // No animation
            finish()
        }

        navRanking.setOnClickListener {
            // Already on this page, do nothing
        }

        navStats.setOnClickListener {
            val intent = Intent(this, Progress_page::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
    }
}