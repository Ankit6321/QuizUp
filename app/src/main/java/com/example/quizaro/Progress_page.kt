package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Progress_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_progress_page)

        // Fix window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.progress_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navigation listeners
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navRanking = findViewById<LinearLayout>(R.id.navRanking)
        val navStats = findViewById<LinearLayout>(R.id.navStats)

        navHome.setOnClickListener {
            if (this !is Home_page) {
                val intent = Intent(this, Home_page::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0) // remove animation
                finish()
            }
        }

        navRanking.setOnClickListener {
            if (this !is Ranking_page) {
                val intent = Intent(this, Ranking_page::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
        }

        navStats.setOnClickListener {
            // Already on Stats (Progress_page), do nothing
        }
    }
}