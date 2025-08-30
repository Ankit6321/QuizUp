package com.example.quizaro

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Home_page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Extend background under system bars (like Progress_page)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)

        // ✅ Fix window insets so content doesn’t overlap status/nav bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.home_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnHost = findViewById<AppCompatButton>(R.id.btnHost)
        val btnJoin = findViewById<AppCompatButton>(R.id.btnJoin)

        // Host button → Go to HostActivity
        btnHost.setOnClickListener {
            val intent = Intent(this, Host_page::class.java)
            startActivity(intent)
        }

        // Join button → Show Join dialog
        btnJoin.setOnClickListener {
            showJoinDialog()
        }

        // ✅ Bottom Navigation Setup
        val navHome = findViewById<LinearLayout>(R.id.navHome)
        val navRanking = findViewById<LinearLayout>(R.id.navRanking)
        val navStats = findViewById<LinearLayout>(R.id.navStats)

        navHome.setOnClickListener {
            // Already on Home → do nothing
        }

        navRanking.setOnClickListener {
            if (this !is Ranking_page) {
                val intent = Intent(this, Ranking_page::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0) // no animation
                finish()
            }
        }

        navStats.setOnClickListener {
            if (this !is Progress_page) {
                val intent = Intent(this, Progress_page::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }
        }
    }

    private fun showJoinDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_join_quiz, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val etCode = dialogView.findViewById<EditText>(R.id.etCode)
        val btnSearch = dialogView.findViewById<AppCompatButton>(R.id.btnSearchQuiz)
        val layoutQuizDetails = dialogView.findViewById<LinearLayout>(R.id.layoutQuizDetails)
        val tvQuizTitle = dialogView.findViewById<TextView>(R.id.tvQuizTitle)
        val tvQuizDescription = dialogView.findViewById<TextView>(R.id.tvQuizTopic)
        val tvHostName = dialogView.findViewById<TextView>(R.id.tvHostName)

        // Search click
        btnSearch.setOnClickListener {
            val code = etCode.text.toString().trim()

            if (code.isEmpty()) {
                etCode.error = "Please enter a code"
            } else {
                if (code == "12345") {
                    layoutQuizDetails.visibility = View.VISIBLE
                    tvQuizTitle.text = "General Knowledge Quiz"
                    tvQuizDescription.text = "Test your GK and challenge friends!"
                    tvHostName.text = "Host: Alice"
                } else {
                    Toast.makeText(this, "Quiz not found!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // remove default grey background
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()
    }
}