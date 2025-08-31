package com.example.quizaro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import com.google.gson.Gson

class quiz_page : AppCompatActivity() {

    private lateinit var tvQuizTitle: TextView
    private lateinit var tvQuizStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var progressBarTimer: ProgressBar
    private lateinit var tvQuestion: TextView
    private lateinit var tvOption1: TextView
    private lateinit var tvOption2: TextView
    private lateinit var tvOption3: TextView
    private lateinit var tvOption4: TextView
    private lateinit var optionsLayout: LinearLayout
    private lateinit var btnSubmit: AppCompatButton
    private lateinit var btnBack: ImageView

    private lateinit var cbOption1: CheckBox
    private lateinit var cbOption2: CheckBox
    private lateinit var cbOption3: CheckBox
    private lateinit var cbOption4: CheckBox

    private lateinit var option1: LinearLayout
    private lateinit var option2: LinearLayout
    private lateinit var option3: LinearLayout
    private lateinit var option4: LinearLayout

    private var quizTimer: CountDownTimer? = null
    private var currentQuestionIndex = 0
    private var quiz: Quiz? = null
    private var isSubmitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.quiz_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // View Bindings
        tvQuizTitle = findViewById(R.id.tv_quiz_title)
        tvQuizStatus = findViewById(R.id.tv_quiz_status)
        tvTimer = findViewById(R.id.tv_timer)
        progressBarTimer = findViewById(R.id.progressBar_timer)
        optionsLayout = findViewById(R.id.options_layout)
        tvQuestion = findViewById(R.id.tv_question)
        tvOption1 = findViewById(R.id.tv_option1)
        tvOption2 = findViewById(R.id.tv_option2)
        tvOption3 = findViewById(R.id.tv_option3)
        tvOption4 = findViewById(R.id.tv_option4)
        btnSubmit = findViewById(R.id.btn_submit)
        btnBack = findViewById(R.id.btn_back)

        cbOption1 = findViewById(R.id.cb_option1)
        cbOption2 = findViewById(R.id.cb_option2)
        cbOption3 = findViewById(R.id.cb_option3)
        cbOption4 = findViewById(R.id.cb_option4)

        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        // Load quiz data from intent
        val quizJson = intent.getStringExtra("QUIZ_JSON")
        if (quizJson != null) {
            quiz = Gson().fromJson(quizJson, Quiz::class.java)
            displayQuestion()
        } else {
            Toast.makeText(this, "Failed to load quiz data.", Toast.LENGTH_LONG).show()
            finish()
        }

        // Handle option selection
        val allCheckboxes = listOf(cbOption1, cbOption2, cbOption3, cbOption4)

        option1.setOnClickListener { handleOptionClick(cbOption1, allCheckboxes) }
        option2.setOnClickListener { handleOptionClick(cbOption2, allCheckboxes) }
        option3.setOnClickListener { handleOptionClick(cbOption3, allCheckboxes) }
        option4.setOnClickListener { handleOptionClick(cbOption4, allCheckboxes) }

        // Handle submit button click
        btnSubmit.setOnClickListener {
            // Only allow submitting if an option is selected and not already submitted
            if (!isSubmitted) {
                val selectedOption = allCheckboxes.firstOrNull { it.isChecked }
                if (selectedOption != null) {
                    isSubmitted = true
                    disableOptions()
                    Toast.makeText(this, "Answer submitted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Back button
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun displayQuestion() {
        quizTimer?.cancel()
        isSubmitted = false // Reset submission flag for the new question

        if (currentQuestionIndex < quiz?.questions?.size ?: 0) {
            val question = quiz!!.questions[currentQuestionIndex]

            tvQuizTitle.text = quiz?.title ?: "Quiz"
            tvQuizStatus.text = "${currentQuestionIndex + 1} of ${quiz?.questions?.size}"
            tvQuestion.text = question.questionText
            tvOption1.text = question.options[0]
            tvOption2.text = question.options[1]
            tvOption3.text = question.options[2]
            tvOption4.text = question.options[3]

            // ✅ Clear all checkboxes for the new question
            clearAllCheckBoxes()
            enableOptions()
            btnSubmit.isEnabled = false // Button is disabled by default

            val timeInMillis = (question.timeLimit * 1000).toLong()
            progressBarTimer.max = timeInMillis.toInt()
            progressBarTimer.progress = timeInMillis.toInt()
            startQuizTimer(timeInMillis)
        } else {
            Toast.makeText(this, "Quiz finished!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun startQuizTimer(totalTime: Long) {
        quizTimer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvTimer.text = "$secondsLeft s"
                progressBarTimer.progress = millisUntilFinished.toInt()
            }

            override fun onFinish() {
                tvTimer.text = "0 s"
                progressBarTimer.progress = 0
                Toast.makeText(this@quiz_page, "Time's up! Moving to the next question.", Toast.LENGTH_SHORT).show()
                disableOptions() // Lock selections when time is up
                moveToNextQuestion()
            }
        }.start()
    }

    private fun moveToNextQuestion() {
        currentQuestionIndex++
        displayQuestion()
    }

    private fun handleOptionClick(selectedCheckbox: CheckBox, allCheckboxes: List<CheckBox>) {
        if (!isSubmitted) {
            allCheckboxes.forEach { it.isChecked = false }
            selectedCheckbox.isChecked = true
            btnSubmit.isEnabled = true
        }
    }

    private fun disableOptions() {
        option1.isClickable = false
        option2.isClickable = false
        option3.isClickable = false
        option4.isClickable = false
        btnSubmit.isEnabled = false
    }

    private fun enableOptions() {
        option1.isClickable = true
        option2.isClickable = true
        option3.isClickable = true
        option4.isClickable = true
        btnSubmit.isEnabled = false
    }

    // ✅ New function to clear all checkboxes
    private fun clearAllCheckBoxes() {
        val allCheckboxes = listOf(cbOption1, cbOption2, cbOption3, cbOption4)
        allCheckboxes.forEach { it.isChecked = false }
    }

    override fun onDestroy() {
        super.onDestroy()
        quizTimer?.cancel()
    }
}