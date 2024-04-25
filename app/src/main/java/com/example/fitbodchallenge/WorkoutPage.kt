package com.example.fitbodchallenge

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fitbodchallenge.ui.ExerciseRow
import com.example.fitbodchallenge.ui.theme.appBarColor

@Preview
@Composable
private fun PreviewWorkoutPageContent() {
    WorkoutPageContent(
        onDetailClicked = {},
        exercises = emptyMap(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPageContent(
    onDetailClicked: (String) -> Unit,
    exercises: Map<String, Int>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Workout") },
                navigationIcon = {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu icon")
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarColor,
                    ),
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
        ) {
            items(exercises.entries.toList()) { (exercise, oneRepMax) ->
                ExerciseRow(
                    onDetailClicked = onDetailClicked,
                    name = exercise,
                    oneRepMax = oneRepMax,
                )
                Divider()
            }
        }
    }
}
