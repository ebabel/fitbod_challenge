package com.example.fitbodchallenge.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fitbodchallenge.MainViewModel
import com.example.fitbodchallenge.ui.theme.outerSpace
import com.example.fitbodchallenge.ui.theme.sunglo
import com.juul.krayon.axis.axisBottom
import com.juul.krayon.axis.axisLeft
import com.juul.krayon.axis.call
import com.juul.krayon.color.Color
import com.juul.krayon.color.transparent
import com.juul.krayon.color.white
import com.juul.krayon.compose.ElementView
import com.juul.krayon.element.CircleElement
import com.juul.krayon.element.GroupElement
import com.juul.krayon.element.LineElement
import com.juul.krayon.element.PathElement
import com.juul.krayon.element.RootElement
import com.juul.krayon.element.TransformElement
import com.juul.krayon.element.withKind
import com.juul.krayon.kanvas.Paint
import com.juul.krayon.kanvas.Transform
import com.juul.krayon.scale.domain
import com.juul.krayon.scale.extent
import com.juul.krayon.scale.range
import com.juul.krayon.scale.scale
import com.juul.krayon.scale.ticks
import com.juul.krayon.selection.append
import com.juul.krayon.selection.asSelection
import com.juul.krayon.selection.data
import com.juul.krayon.selection.each
import com.juul.krayon.selection.join
import com.juul.krayon.selection.selectAll
import com.juul.krayon.shape.line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.atTime
import kotlinx.datetime.format.MonthNames

@Preview
@Composable
private fun PreviewGraph() {
    Column(Modifier.fillMaxSize()) {
        Graph(
            emptyFlow(),
        )
    }
}


private val solidLinePaint = Paint.Stroke(sunglo, 2f)
private val gridLinePaint = Paint.Stroke(outerSpace, 1f)
private val circlePaint = Paint.FillAndStroke(
    Paint.Fill(sunglo),
    Paint.Stroke(sunglo, 1f),
)

internal fun lineChart(
    root: RootElement,
    width: Float,
    height: Float,
    data: List<MainViewModel.GraphPoint>
) {
    val dataWithTime = data.map { it.date.atTime(0,0) to it.oneRepMax.toFloat() }
    val leftMargin = 55f
    val topMargin = 20f
    val rightMargin = 0f
    val bottomMargin = 40f

    val innerWidth = width - leftMargin - rightMargin
    val innerHeight = height - topMargin - bottomMargin

    val x = scale()
        .domain(dataWithTime.extent { it.first })
        .range(0f, innerWidth)
    val y = scale()
        .domain(dataWithTime.minOf { it.second }, dataWithTime.maxOf { it.second })
        .range(innerHeight, 0f)

    val line = line<Pair<LocalDateTime, Float>>()
        .x { (p) -> x.scale(p.first) }
        .y { (p) -> y.scale(p.second) }

    val body = root.asSelection()
        .selectAll(TransformElement.withKind("body"))
        .data(listOf(null))
        .join { append(TransformElement).each { kind = "body" } }
        .each {
            transform = Transform.Translate(
                horizontal = leftMargin,
                vertical = topMargin,
            )
        }


    val localDateTimes = dataWithTime.map { it.first }
    body.selectAll(LineElement.withKind("vertical-grid-line"))
        .data(ticks(localDateTimes.min(), localDateTimes.max(), 5))
        .join {
            append(LineElement).each {
                kind = "vertical-grid-line"
            }
        }.each { (data, _) ->
            startX = x.scale(data)
            endX = startX
            startY = y.range.min()
            endY = y.range.max()
            paint = gridLinePaint
        }

    val oneRepMaxes = dataWithTime.map { it.second }
    body.selectAll(LineElement.withKind("horizontal-grid-line"))
        .data(ticks(oneRepMaxes.min(), oneRepMaxes.max(), 5))
        .join {
            append(LineElement).each {
                kind = "horizontal-grid-line"
            }
        }.each { (data, _) ->
            startX = x.range.min()
            endX = x.range.max()
            startY = y.scale(data)
            endY = startY
            paint = gridLinePaint
        }

    body.selectAll(TransformElement.withKind("x-axis"))
        .data(listOf(null))
        .join {
            append(TransformElement).each {
                kind = "x-axis"
            }
        }
        .each { transform = Transform.Translate(vertical = innerHeight) }
        .call(axisBottom(x).apply {
            textColor = white
            formatter = {
                val month = MonthNames.ENGLISH_ABBREVIATED.names[it.monthNumber - 1]
                "$month ${it.dayOfMonth}"
            }
            lineColor = transparent
        })

    body.selectAll(GroupElement.withKind("y-axis"))
        .data(listOf(null))
        .join {
            append(GroupElement).each {
                kind = "y-axis"
            }
        }
        .call(axisLeft(y).apply {
            textColor = white
            formatter = {
                "${it.toInt()} lbs"
            }
            lineColor = transparent
        })

    body.selectAll(PathElement.withKind("line"))
        .data(listOf(dataWithTime, dataWithTime))
        .join {
            append(PathElement).each { (_, _) ->
                kind = "line"
                paint = solidLinePaint
            }
        }.each { (d) ->
            path = line.render(d)
        }

    body.selectAll(CircleElement)
        .data(dataWithTime)
        .join {
            append(CircleElement).each {
                radius = 3f
                paint = circlePaint
            }
        }.each { (d) ->
            centerX = x.scale(d.first)
            centerY = y.scale(d.second)
        }
}

@Composable
fun Graph(
    exerciseGraphDataFlow: Flow<List<MainViewModel.GraphPoint>>
) {
    ElementView(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .height(400.dp),
        dataSource = exerciseGraphDataFlow,
        updateElements = ::lineChart
    )
}
