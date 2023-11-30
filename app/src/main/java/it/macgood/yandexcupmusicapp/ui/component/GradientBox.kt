package it.macgood.yandexcupmusicapp.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import it.macgood.yandexcupmusicapp.ui.MusicViewModel
import it.macgood.yandexcupmusicapp.R
import kotlin.math.abs

@Composable
fun GradientBox(
    musicViewModel: MusicViewModel = hiltViewModel(),
    visible: Boolean = false,
    content: @Composable () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5A50E2), Color.Transparent),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .zIndex(1f)
            .padding(horizontal = 12.dp)
            .background(brush = gradient),
        contentAlignment = Alignment.BottomStart,
    ) {
        var sliderPositionHorizontal by rememberSaveable { mutableStateOf(100f) }
        var sliderPositionVertical by rememberSaveable { mutableStateOf(-300f) }
        var stateHeight by remember { mutableStateOf(0f) }

        Canvas(modifier = Modifier
            .fillMaxSize()

            .alpha(if (visible) 0f else 1f)) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            stateHeight = canvasHeight
            val lineLength = 24.dp.toPx()
            val strokeWidth = 2.dp.toPx()
            val lineColor = Color.White

            val offset = 2.dp.toPx()

            // Рисуем вертикальные линии (Скорость)
            for (i in 0 until 30) {
                var startX = (canvasWidth / 30) * i + offset
                var startY = canvasHeight - lineLength
                if (i in 0..2) {
                    continue
                }
                if (i > 10) {
                    if (i > 15) {
                        for (j in 0 until 5) {
                            drawLine(
                                color = lineColor,
                                start = Offset(startX, startY),
                                end = Offset(startX, canvasHeight - offset),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        }
                    } else {
                        for (j in 0 until 5) {
                            drawLine(
                                color = lineColor,
                                start = Offset(startX, startY),
                                end = Offset(startX, canvasHeight - offset),
                                strokeWidth = strokeWidth,
                                cap = StrokeCap.Round
                            )
                            startX = (canvasWidth / 25) * i + offset * 5
                        }
                    }
                    for (j in 0 until 5) {
                        drawLine(
                            color = lineColor,
                            start = Offset(startX, startY),
                            end = Offset(startX, canvasHeight - offset),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                        startX = (canvasWidth / 25) * i + offset * 5
                    }

                } else {
                    drawLine(
                        color = lineColor,
                        start = Offset(startX, startY),
                        end = Offset(startX, canvasHeight - offset),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }

                // Рисуем горизонтальные линии (Громкость)
                for (i in 0 until 25) {
                    val startY = (canvasHeight / 25) * i
                    if (i in 23..25) {
                        continue
                    }
                    if (i % 5 == 0) {
                        drawLine(
                            color = lineColor,
                            start = Offset(offset, startY),
                            end = Offset(lineLength + offset, startY),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    } else {
                        drawLine(
                            color = lineColor,
                            start = Offset(offset, startY),
                            end = Offset((lineLength / 2) + offset, startY),
                            strokeWidth = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .offset { IntOffset(sliderPositionHorizontal.toInt(), 0) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        if (sliderPositionHorizontal in 50f..750f) {
                            sliderPositionHorizontal += dragAmount.x
                                .coerceIn(-500f, 500f)
                                .toInt()
                            change.consume()
                        } else {
                            if (sliderPositionHorizontal > 750f) {
                                sliderPositionHorizontal = 750f
                            }
                            if (sliderPositionHorizontal < 50f) {
                                sliderPositionHorizontal = 50f
                            }
                        }

                        val speedRanges = listOf(
                            50f..100f to 0.5f,
                            101f..350f to 1f,
                            351f..420f to 2f,
                            421f..500f to 3f,
                            501f..650f to 4f,
                            651f..Float.MAX_VALUE to 5f
                        )

                        for ((range, speed) in speedRanges) {
                            if (sliderPositionHorizontal in range) {
                                musicViewModel.setSpeed(speed)
                                break
                            }
                        }
                    }
                },
            shape = RectangleShape,
        ) {
            Image(
                modifier = Modifier
                    .height(25.dp)
                    .width(90.dp),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.ic_speed_tumb),
                contentDescription = null
            )
        }

        // Вертикальный ползунок
        Surface(
            modifier = Modifier
                .offset { IntOffset(0, sliderPositionVertical.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->

                        var changingPos = sliderPositionVertical + dragAmount.y.coerceIn(
                            -500f,
                            500f
                        )
                        if (changingPos in -940f..-27f) {
                            sliderPositionVertical += dragAmount.y.coerceIn(
                                -500f,
                                500f
                            )
                        }

                        val volumeRanges = listOf(
                            27f..100f to musicViewModel.maxVolume - musicViewModel.maxVolume,
                            101f..181f to musicViewModel.maxVolume / 7f,
                            182f..235f to musicViewModel.maxVolume / 4.5f,
                            236f..289f to musicViewModel.maxVolume / 3.5f,
                            290f..343f to musicViewModel.maxVolume / 3.1f,
                            344f..397f to musicViewModel.maxVolume / 2.9f,
                            398f..451f to musicViewModel.maxVolume / 2.7f,
                            452f..505f to musicViewModel.maxVolume / 2.5f,
                            506f..559f to musicViewModel.maxVolume / 2.3f,
                            560f..613f to musicViewModel.maxVolume / 2.1f,
                            614f..667f to musicViewModel.maxVolume / 1.9f,
                            668f..721f to musicViewModel.maxVolume / 1.7f,
                            722f..775f to musicViewModel.maxVolume / 1.5f,
                            776f..829f to musicViewModel.maxVolume / 1.3f,
                            830f..883f to musicViewModel.maxVolume / 1.1f,
                            884f..940f to musicViewModel.maxVolume
                        )

                        for ((range, volume) in volumeRanges) {
                            if (abs(sliderPositionVertical) in range) {
                                musicViewModel.setVolume(volume)
                                break
                            }
                        }

                        change.consume()
                    }
                },
            shape = RectangleShape,
        ) {
            Image(
                modifier = Modifier
                    .height(90.dp)
                    .width(25.dp),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.drawable.ic_sound_thumb),
                contentDescription = null
            )
        }
    }
}

@Composable
fun GradientBoxEmpty(
    content: @Composable () -> Unit
) {
    var value by remember { mutableStateOf(0.0f) }
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF5A50E2), Color.Transparent),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(472.dp)
            .zIndex(1f)
            .padding(horizontal = 12.dp)
            .background(brush = gradient),
        contentAlignment = Alignment.BottomStart,
    ) {
        content()
    }
}