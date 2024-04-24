@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitbodchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.W300
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitbodchallenge.ui.Graph
import com.example.fitbodchallenge.ui.theme.FitbodChallengeTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


private const val ROUTE_EXERCISE_DETAIL_PARAM_NAME = "name"
private const val ROUTE_EXERCISE_DETAIL = "ExerciseDetail/{$ROUTE_EXERCISE_DETAIL_PARAM_NAME}"
private const val ROUTE_HOME = "Home"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FitbodChallengeTheme {
                val navController = rememberNavController()
                val viewModel = viewModel<MainViewModel>()
                NavHost(
                    navController = navController,
                    startDestination = ROUTE_HOME,
                ) {
                    composable(ROUTE_HOME) {
                        WorkoutPageContent(
                            onDetailClicked = { name ->
                                navController.navigate(
                                    ROUTE_EXERCISE_DETAIL.replace(
                                        "{$ROUTE_EXERCISE_DETAIL_PARAM_NAME}",
                                        URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
                                    )
                                )
                            },
                            exercises = viewModel.exercises.collectAsState(emptyMap()).value,
                        )
                    }
                    composable(ROUTE_EXERCISE_DETAIL) { navBackStackEntry ->
                        BackHandler {
                            navController.popBackStack()
                        }
                        ExerciseDetailPageContent(
                            onNavigateBack = { navController.popBackStack() },
                            exercise = navBackStackEntry
                                .arguments
                                ?.getString(ROUTE_EXERCISE_DETAIL_PARAM_NAME)
                                .let {
                                    URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                                } ?: error("Unexpectedly null argument for name")
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun PreviewExerciseDetailPageContent() {
    ExerciseDetailPageContent(
        exercise = "Back Squat",
        onNavigateBack = {}
    )
}

@Composable
private fun ExerciseDetailPageContent(
    exercise: String,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = exercise) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Menu icon")
                    }
                },
                colors = topAppBarColors(
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
                name = "exercise",
                oneRepMax = 1
            )
            Graph()
        }
    }
}

@Preview
@Composable
private fun PreviewWorkoutPageContent() {
    WorkoutPageContent(
        onDetailClicked = {},
        exercises = emptyMap(),
    )
}

@Composable
private fun WorkoutPageContent(
    onDetailClicked: (String) -> Unit,
    exercises: Map<String, Int>
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Workout") },
                navigationIcon = {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu icon")
                },
                colors = topAppBarColors(
                    containerColor = Color(0xFFE07161),
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
        ) {
            items(exercises.entries.toList()) { (exercise, oneRepMax) ->
                ExerciseRow(
                    onDetailClicked = onDetailClicked,
                    name = exercise,
                    oneRepMax = oneRepMax
                )
                Divider()
            }
        }
    }

}

@Preview
@Composable
private fun PreviewExerciseRow() {
    Column(Modifier.fillMaxSize()) {
        ExerciseRow(onDetailClicked = {}, name = "example", oneRepMax = 1)
    }
}


@Composable
private fun ExerciseRow(
    modifier: Modifier = Modifier,
    onDetailClicked: (String) -> Unit,
    name: String,
    oneRepMax: Int,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clickable {
                onDetailClicked(name)
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = name, fontWeight = W500)
            Text(text = "1 RM Record", textAlign = TextAlign.End)
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = oneRepMax.toString(), fontWeight = W500)
            Text(text = "lbs", fontWeight = W300, textAlign = TextAlign.End)
        }
    }
}
