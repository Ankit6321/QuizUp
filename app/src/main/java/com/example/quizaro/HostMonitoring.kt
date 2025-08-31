package com.example.quizaro

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.FileReader

class HostMonitoringActivity : AppCompatActivity() {

    private lateinit var quizTitle: TextView
    private lateinit var quizTopic: TextView
    private lateinit var roomCode: TextView // New TextView for the room code
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
        roomCode = findViewById(R.id.tvRoomCode) // Bind the new TextView
        startQuizBtn = findViewById(R.id.startQuizBtn)
        participantsRecycler = findViewById(R.id.participantsRecycler)

        // Get the quiz code passed from Host_page
        val uniqueCode = intent.getStringExtra("QUIZ_CODE")

        if (uniqueCode != null) {
            val fileName = "quiz_$uniqueCode.json"
            val file = File(filesDir, fileName)

            if (file.exists()) {
                try {
                    // Read and parse the JSON file
                    val gson = Gson()
                    val quiz = gson.fromJson(FileReader(file), Quiz::class.java)

                    // Set quiz title and topic from the JSON file
                    quizTitle.text = quiz.title
                    quizTopic.text = "Topic: ${quiz.topic}"
                    roomCode.text = uniqueCode // Set the room code here

                } catch (e: JsonSyntaxException) {
                    // Handle a corrupted JSON file
                    quizTitle.text = "Error loading quiz"
                    quizTopic.text = "Topic: N/A"
                    roomCode.text = "Error"
                    e.printStackTrace()
                }
            } else {
                // Handle case where file does not exist
                quizTitle.text = "Quiz not found"
                quizTopic.text = "Topic: N/A"
                roomCode.text = "N/A"
            }
        } else {
            // Handle case where no code was passed
            quizTitle.text = "Untitled Quiz"
            quizTopic.text = "Topic: General"
            roomCode.text = "N/A"
        }

        // RecyclerView setup
        adapter = ParticipantsAdapter(participantsList)
        participantsRecycler.layoutManager = LinearLayoutManager(this)
        participantsRecycler.adapter = adapter

        // Start Quiz button
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