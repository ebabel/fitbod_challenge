package com.example.fitbodchallenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitbodchallenge.model.Exercise
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames

private const val FILE_LOCATION = "https://raw.githubusercontent.com/ebabel/fitbod_challenge/main/workoutData"

class MainViewModel : ViewModel() {


    private val ktorHttpClient = HttpClient(Android)
    private val _workoutData = MutableStateFlow<List<Exercise>>(emptyList())
    val exercises = _workoutData.map { it.groupBy { it.exercise }.mapValues { it.value.maxOf { it.weight } } }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val workoutData: String =
                ktorHttpClient.get(FILE_LOCATION)
            workoutData
                .lines()
                .filter { it.isNotEmpty() }
                .map { it.split(",") }
                .map {
                    println(it)
                    val date = it[0].split(" ")
                    val monthNumber = MonthNames.ENGLISH_ABBREVIATED.names.indexOf(date[0]) + 1
                    println("month ${date[0]} $monthNumber")
                    val localDate = LocalDate(
                        dayOfMonth = date[1].toInt(),
                        monthNumber = monthNumber,
                        year = date[2].toInt(),
                    )
                    Exercise(date = localDate, exercise = it[1], reps = it[2].toInt(), weight = it[3].toInt())
                }.let {
                    _workoutData.value = it
                }
        }

    }
}