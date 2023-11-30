package it.macgood.core_ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.White,
    containerColor: Color,
    onClick: () -> Unit = {}
) {
    var glareColor: Color
    var savedContainerColor = containerColor

    val animation = rememberInfiniteTransition()
    val animateFloat = animation.animateFloat(
        initialValue = -0.2f,
        targetValue = 1.01f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                delayMillis = 5000,
                easing = LinearOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = ""
    ).apply {
        glareColor = if (this.value > 0f) Color(0xFF3D8EFF)
        else savedContainerColor
    }

    val gradient: Brush = Brush.horizontalGradient(
        0.0f to containerColor,
        animateFloat.value to glareColor,
        1f to containerColor
    )

    Button(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .background(gradient)
                .padding(
                    horizontal = 8.dp, vertical = 8.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = textColor)
        }
    }
}

