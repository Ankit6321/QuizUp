package com.example.quizaro

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HostMonitoringActivity : AppCompatActivity() {

    private lateinit var quizTitle: TextView
    private lateinit var quizTopic: TextView
    private lateinit var startQuizBtn: Button
    private lateinit var participantsRecycler: RecyclerView
    private lateinit var adapter: ParticipantsAdapter

    private val participantsList = mutableListOf(
        "Alice", "Bob", "Charlie" // sample data
    )

    private var quizStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_monitoring)

        quizTitle = findViewById(R.id.quizTitleText)
        quizTopic = findViewById(R.id.quizTopicText)
        startQuizBtn = findViewById(R.id.startQuizBtn)
        participantsRecycler = findViewById(R.id.participantsRecycler)

        val title = intent.getStringExtra("QUIZ_TITLE") ?: "Untitled Quiz"
        val topic = intent.getStringExtra("QUIZ_TOPIC") ?: "General"

        quizTitle.text = title
        quizTopic.text = "Topic: $topic"

        // ✅ RecyclerView setup
        adapter = ParticipantsAdapter(participantsList)
        participantsRecycler.layoutManager = LinearLayoutManager(this)
        participantsRecycler.adapter = adapter

        // ✅ Start Quiz button
        startQuizBtn.setOnClickListener {
            quizStarted = true
            startQuizBtn.isEnabled = false
            startQuizBtn.text = "Quiz Started"

            // simulate participant order update
            participantsList.shuffle()
            adapter.notifyDataSetChanged()
        }
    }
}
