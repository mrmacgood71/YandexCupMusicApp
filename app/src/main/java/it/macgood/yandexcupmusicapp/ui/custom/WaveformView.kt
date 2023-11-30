package it.macgood.yandexcupmusicapp.ui.custom

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun WaveformView(waveform: List<Float>) {

    val path = remember { Path() }

    Canvas(modifier = Modifier
        .height(24.dp)
        .fillMaxWidth(0.9f), onDraw = {

        val barWidth = 2f

        path.reset()

        for (i in waveform.indices) {
            val x = i * barWidth

            val y: Float = when {
                waveform[i] < -41f -> {
                    -41f
                }

                waveform[i] in -40f..-11f -> {
                    waveform[i] / 2
                }

                waveform[i] in -10f..10f -> {
                    waveform[i] * 2
                }

                waveform[i] in 11f..40f -> {
                    waveform[i] / 2
                }

                waveform[i] > 41f -> {
                    30f
                }

                else -> {
                    waveform[i]
                }
            }

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(path, Color.White)
    })
}