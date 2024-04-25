@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.fitbodchallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitbodchallenge.ui.ExerciseDetailPageContent
import com.example.fitbodchallenge.ui.ExerciseRow
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
                val exercises = viewModel.exercises.collectAsState(emptyMap()).value
                NavHost(
                    navController = navController,
                    startDestination = ROUTE_HOME,
                ) {
                    composable(ROUTE_HOME) {
                        WorkoutPageContent(
                            onDetailClicked = { name ->
                                navController.navigate(
                                    ROUTE_EXERCISE_DETAIL.withParam(name)
                                )
                            },
                            exercises = exercises,
                        )
                    }
                    composable(ROUTE_EXERCISE_DETAIL) { navBackStackEntry ->
                        BackHandler { navController.popBackStack() }
                        val name = exerciseNameArgument(navBackStackEntry)
                        ExerciseDetailPageContent(
                            onNavigateBack = { navController.popBackStack() },
                            exercise = name,
                            exercises = viewModel.exerciseDetails(name).collectAsState(initial = emptyList()).value,
                            exerciseGraphDataFlow = viewModel.exerciseGraphDataSource(name),
                        )
                    }
                }

            }
        }
    }
}

private fun String.withParam(name: String) = replace(
    "{$ROUTE_EXERCISE_DETAIL_PARAM_NAME}",
    URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
)

@Composable
private fun exerciseNameArgument(navBackStackEntry: NavBackStackEntry) = navBackStackEntry
    .arguments
    ?.getString(ROUTE_EXERCISE_DETAIL_PARAM_NAME)
    .let {
        URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
    } ?: error("Unexpectedly null argument for name")

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

