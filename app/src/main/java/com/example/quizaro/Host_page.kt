package com.example.quizaro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Host_page : AppCompatActivity() {

    private lateinit var questionInput: EditText
    private lateinit var addQuestionBtn: AppCompatButton
    private lateinit var hostBtn: AppCompatButton
    private lateinit var questionsContainer: LinearLayout
    private lateinit var emptyText: TextView
    private lateinit var questionsCount: TextView

    private lateinit var option1: EditText
    private lateinit var option2: EditText
    private lateinit var option3: EditText
    private lateinit var option4: EditText

    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var checkBox4: CheckBox

    private lateinit var timeLimitSpinner: Spinner

    private val questionsList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_host_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.host_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Spinner setup
        timeLimitSpinner = findViewById(R.id.timeLimitSpinner)
        val timeOptions = listOf("Select time limit", "10 seconds", "20 seconds", "30 seconds", "60 seconds")

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            timeOptions
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_gray))
                } else {
                    view.setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_dark))
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_white))
                return view
            }
        }
        timeLimitSpinner.adapter = adapter

        // 🔹 Views
        questionInput = findViewById(R.id.questionInput)
        addQuestionBtn = findViewById(R.id.addQuestionBtn)
        hostBtn = findViewById(R.id.hostBtn)
        questionsContainer = findViewById(R.id.questionsContainer)
        emptyText = findViewById(R.id.emptyText)
        questionsCount = findViewById(R.id.questionsCount)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2 = findViewById(R.id.checkBox2)
        checkBox3 = findViewById(R.id.checkBox3)
        checkBox4 = findViewById(R.id.checkBox4)

        // 🔹 Add Question Button
        addQuestionBtn.setOnClickListener {
            val question = questionInput.text.toString().trim()
            val opt1 = option1.text.toString().trim()
            val opt2 = option2.text.toString().trim()
            val opt3 = option3.text.toString().trim()
            val opt4 = option4.text.toString().trim()
            val selectedTime = timeLimitSpinner.selectedItem.toString()

            when {
                question.isEmpty() -> {
                    Toast.makeText(this, "Please enter a question", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                opt1.isEmpty() || opt2.isEmpty() || opt3.isEmpty() || opt4.isEmpty() -> {
                    Toast.makeText(this, "Please enter all options", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                !(checkBox1.isChecked || checkBox2.isChecked || checkBox3.isChecked || checkBox4.isChecked) -> {
                    Toast.makeText(this, "Please select at least one correct option", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                selectedTime == "Select time limit" -> {
                    Toast.makeText(this, "Please select a time limit", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // ✅ Add question
            questionsList.add("$question (Time: $selectedTime)")

            if (emptyText.parent != null) {
                questionsContainer.removeView(emptyText)
            }

            val questionView = TextView(this).apply {
                text = "${questionsList.size}. $question (Time: $selectedTime)"
                setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_dark))
                textSize = 16f
                setPadding(8, 8, 8, 8)
            }

            questionsContainer.addView(questionView)
            questionsCount.text = "Questions (${questionsList.size})"

            // Clear inputs
            questionInput.text.clear()
            option1.text.clear()
            option2.text.clear()
            option3.text.clear()
            option4.text.clear()
            checkBox1.isChecked = false
            checkBox2.isChecked = false
            checkBox3.isChecked = false
            checkBox4.isChecked = false
            timeLimitSpinner.setSelection(0)
        }

        // 🔹 Host Button
        hostBtn.setOnClickListener {
            if (questionsList.isEmpty()) {
                Toast.makeText(this, "Please add at least one question", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, HostMonitoringActivity::class.java).apply {
                putStringArrayListExtra("QUESTIONS", ArrayList(questionsList))
            }
            startActivity(intent)
        }
    }
}
