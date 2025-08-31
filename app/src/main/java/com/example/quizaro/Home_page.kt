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
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.FileReader


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
            // Check if not already on Ranking_page to avoid redundant activity creation
            if (this !is Ranking_page) {
                val intent = Intent(this, Ranking_page::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0) // no animation
                finish()
            }
        }

        navStats.setOnClickListener {
            // Check if not already on Progress_page to avoid redundant activity creation
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
        val tvQuizTopic = dialogView.findViewById<TextView>(R.id.tvQuizTopic)
        val tvHostName = dialogView.findViewById<TextView>(R.id.tvHostName)
        val btnJoinNow = dialogView.findViewById<AppCompatButton>(R.id.btnJoinNow)

        // Search click
        btnSearch.setOnClickListener {
            val enteredCode = etCode.text.toString().trim()

            if (enteredCode.isEmpty()) {
                etCode.error = "Please enter a code"
                layoutQuizDetails.visibility = View.GONE
                return@setOnClickListener
            }

            val quizFile = File(filesDir, "quiz_$enteredCode.json")
            if (quizFile.exists()) {
                try {
                    val gson = Gson()
                    val quiz = gson.fromJson(FileReader(quizFile), Quiz::class.java)

                    layoutQuizDetails.visibility = View.VISIBLE
                    tvQuizTitle.text = quiz.title
                    tvQuizTopic.text = quiz.topic
                    tvHostName.text = "Host: You"

                    // This is the CORRECT place to define the listener.
                    // It is only set if the search is successful.
                    btnJoinNow.setOnClickListener {
                        val intent = Intent(this, quiz_page::class.java).apply {
                            putExtra("QUIZ_JSON", gson.toJson(quiz))
                        }
                        startActivity(intent)
                        dialog.dismiss()
                    }

                } catch (e: Exception) {
                    // Catching a general exception to handle both file and JSON parsing errors
                    Toast.makeText(this, "An error occurred while loading the quiz.", Toast.LENGTH_SHORT).show()
                    layoutQuizDetails.visibility = View.GONE
                }
            } else {
                Toast.makeText(this, "Quiz not found!", Toast.LENGTH_SHORT).show()
                layoutQuizDetails.visibility = View.GONE
            }
        }

        // The old, incorrect btnJoinNow.setOnClickListener block has been removed from here.

        // remove default grey background
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}