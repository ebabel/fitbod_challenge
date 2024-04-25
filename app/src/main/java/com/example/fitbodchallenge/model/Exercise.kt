package com.example.fitbodchallenge.model

import kotlinx.datetime.LocalDate

data class Exercise(
    val date: LocalDate,
    val name: String,
    val reps: Int,
    val weight: Int,
    val brzycki: Float,
)
