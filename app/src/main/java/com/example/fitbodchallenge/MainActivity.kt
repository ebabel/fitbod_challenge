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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.W300
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitbodchallenge.ui.Graph
import com.example.fitbodchallenge.ui.theme.FitbodChallengeTheme

private const val ROUTE_EXERCISE_DETAIL = "ExerciseDetail"
private const val ROUTE_HOME = "Home"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitbodChallengeTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = ROUTE_HOME,
                ) {
                    composable(ROUTE_HOME) {
                        WorkoutPageContent(
                            onDetailClicked = {
                                navController.navigate(ROUTE_EXERCISE_DETAIL)
                            })
                    }
                    composable(ROUTE_EXERCISE_DETAIL) {
                        BackHandler {
                            navController.popBackStack()
                        }
                        ExerciseDetailPageContent()
                    }
                }

            }
        }
    }
}

@Preview
@Composable
private fun PreviewExerciseDetailPageContent() {
    ExerciseDetailPageContent()
}

@Composable
private fun ExerciseDetailPageContent() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Back Squat") },
                navigationIcon = {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu icon")
                }
            )
        }
    ) { paddingValues ->
        Column(Modifier.padding(paddingValues).fillMaxSize()) {
            ExerciseRow(
                onDetailClicked = {},
            )
            Graph()
        }
    }
}

@Preview
@Composable
private fun PreviewWorkoutPageContent() {
    WorkoutPageContent {

    }
}

@Composable
private fun WorkoutPageContent(onDetailClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Workout") }, navigationIcon = {
                Icon(Icons.Filled.Menu, contentDescription = "Menu icon")
            })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                ExerciseRow(
                    onDetailClicked = onDetailClicked
                )
            }
        }
    }

}

@Preview
@Composable
private fun PreviewExerciseRow() {
    Column(Modifier.fillMaxSize()) {
        ExerciseRow(onDetailClicked = {})
    }
}


@Composable
private fun ExerciseRow(
    modifier: Modifier = Modifier,
    onDetailClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onDetailClicked()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(horizontal = 15.dp)) {
            Text(text = "Back Squat", fontWeight = W500)
            Text(text = "1 RM Record", textAlign = TextAlign.End)
        }
        Column(
            modifier = Modifier.padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "215", fontWeight = W500)
            Text(text = "lbs", fontWeight = W300, textAlign = TextAlign.End)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FitbodChallengeTheme {
        Greeting("Android")
    }
}