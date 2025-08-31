package com.example.quizaro

data class QuestionData(
    val questionText: String,
    val timeLimit: String,
    val options: List<String>,
    val correctOptionIndex: Int
)