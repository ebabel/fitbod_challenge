package com.example.fitbodchallenge.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
private fun PreviewExerciseRow() {
    Column(Modifier.fillMaxSize()) {
        ExerciseRow(onDetailClicked = {}, name = "example", oneRepMax = 1)
    }
}
@Composable
fun ExerciseRow(
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
            Text(text = name, fontWeight = FontWeight.W500)
            Text(text = "1 RM Record", textAlign = TextAlign.End, style = TextStyle(color = Color(0xFF7F8383)))
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = oneRepMax.toString(), fontWeight = FontWeight.W500)
            Text(text = "lbs", fontWeight = FontWeight.W300, textAlign = TextAlign.End, style = TextStyle(color = Color(0xFF7F8383)))
        }
    }
}
