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
import android.view.ViewGroup
import com.google.gson.Gson
import java.io.File
import java.io.FileWriter
import java.util.UUID

class Host_page : AppCompatActivity() {

    private lateinit var quizTitleInput: EditText
    private lateinit var topicInput: EditText
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

    // Use a list of the new QuestionData class to store input
    private val questionsList = mutableListOf<QuestionData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_host_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.host_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 View Bindings
        quizTitleInput = findViewById(R.id.quizTitleInput)
        topicInput = findViewById(R.id.topicInput)
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
        timeLimitSpinner = findViewById(R.id.timeLimitSpinner)

        // 🔹 Spinner setup
        val timeOptions = listOf("Select time limit", "10 seconds", "20 seconds", "30 seconds", "60 seconds")
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            timeOptions
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_gray))
                } else {
                    view.setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_dark))
                }
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(ContextCompat.getColor(this@Host_page, R.color.text_white))
                return view
            }
        }
        timeLimitSpinner.adapter = adapter

        // 🔹 Checkbox listeners
        val checkboxes = arrayOf(checkBox1, checkBox2, checkBox3, checkBox4)
        for (i in checkboxes.indices) {
            checkboxes[i].setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    for (j in checkboxes.indices) {
                        if (i != j) {
                            checkboxes[j].isChecked = false
                        }
                    }
                }
            }
        }

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
                    Toast.makeText(this, "Please select the correct option", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                selectedTime == "Select time limit" -> {
                    Toast.makeText(this, "Please select a time limit", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Get the correct option index
            val correctIndex = when {
                checkBox1.isChecked -> 0
                checkBox2.isChecked -> 1
                checkBox3.isChecked -> 2
                checkBox4.isChecked -> 3
                else -> -1
            }

            // ✅ Store complete question data in the list
            val newQuestion = QuestionData(
                questionText = question,
                timeLimit = selectedTime,
                options = listOf(opt1, opt2, opt3, opt4),
                correctOptionIndex = correctIndex
            )
            questionsList.add(newQuestion)

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
            val quizTitle = quizTitleInput.text.toString().trim()
            val quizTopic = topicInput.text.toString().trim()

            when {
                quizTitle.isEmpty() -> {
                    Toast.makeText(this, "Please enter a quiz title", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                quizTopic.isEmpty() -> {
                    Toast.makeText(this, "Please enter a quiz topic", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                questionsList.isEmpty() -> {
                    Toast.makeText(this, "Please add at least one question", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Generate a unique code
            val uniqueCode = UUID.randomUUID().toString().substring(0, 5)

            // ✅ Map the temporary data to the final data structure
            val questionsForJson = questionsList.map { questionData ->
                Question(
                    questionText = questionData.questionText,
                    timeLimit = questionData.timeLimit.substringBefore(" ").toInt(),
                    options = questionData.options,
                    correctOptionIndex = questionData.correctOptionIndex
                )
            }

            val quizData = Quiz(
                code = uniqueCode,
                title = quizTitle,
                topic = quizTopic,
                questions = questionsForJson
            )

            // Save the quiz data as a JSON file
            val gson = Gson()
            val jsonString = gson.toJson(quizData)
            val fileName = "quiz_$uniqueCode.json"
            val file = File(filesDir, fileName)

            try {
                FileWriter(file).use { writer ->
                    writer.write(jsonString)
                }
                Toast.makeText(this, "Quiz created with code: $uniqueCode", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to save quiz data", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            val intent = Intent(this, HostMonitoringActivity::class.java).apply {
                putExtra("QUIZ_TITLE", quizTitle)
                putExtra("QUIZ_TOPIC", quizTopic)
                putExtra("QUIZ_CODE", uniqueCode)
            }
            startActivity(intent)
        }
    }
}