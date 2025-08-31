package com.example.quizaro

data class Quiz(
    val code: String,
    val title: String,
    val topic: String,
    val questions: List<Question>
)

data class Question(
    val questionText: String,
    val timeLimit: Int,
    val options: List<String>,
    val correctOptionIndex: Int
)