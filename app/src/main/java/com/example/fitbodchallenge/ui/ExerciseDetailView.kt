@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitbodchallenge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitbodchallenge.MainViewModel
import com.example.fitbodchallenge.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow


@Preview
@Composable
private fun PreviewExerciseDetailPageContent() {
    ExerciseDetailPageContent(
        exercise = "Back Squat",
        onNavigateBack = {},
        exercises = emptyList(),
        exerciseGraphDataFlow = emptyFlow(),
    )
}

@Composable
fun ExerciseDetailPageContent(
    exercise: String,
    onNavigateBack: () -> Unit,
    exercises: List<Exercise>,
    exerciseGraphDataFlow: Flow<List<MainViewModel.GraphPoint>>
) {
    if (exercises.isEmpty()) {
        Text("No data")
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = exercise) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Menu icon")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE07161),
                )
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ExerciseRow(
                onDetailClicked = {},
                name = exercise,
                oneRepMax = exercises.maxOf { it.weight }
            )
            Graph(
                exerciseGraphDataFlow = exerciseGraphDataFlow
            )
        }
    }
}
