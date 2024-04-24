package com.example.fitbodchallenge.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun PreviewGraph() {
    Column(Modifier.fillMaxSize()) {
        Graph()
    }
}

@Composable
fun Graph(
) {
    Canvas(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
            .clipToBounds()
    ) {

        Path().apply {


            moveTo( 0f, 0f)
            lineTo( 100f, 100f)
            drawPath(
                path = this,
                color = Color.White,
                style = Stroke(width = 2f)
            )
        }
    }
}