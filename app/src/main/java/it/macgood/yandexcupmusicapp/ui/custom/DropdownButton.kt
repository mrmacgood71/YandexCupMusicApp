package it.macgood.yandexcupmusicapp.ui.custom

import android.util.Log
import android.view.MotionEvent
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import it.macgood.yandexcupmusicapp.R
import it.macgood.yandexcupmusicapp.ui.YGreen
import it.macgood.yandexcupmusicapp.ui.custom.DropdownButtonDefaults.gradientDefault
import it.macgood.yandexcupmusicapp.ui.custom.DropdownButtonDefaults.gradientHover
import it.macgood.yandexcupmusicapp.ui.custom.DropdownButtonDefaults.gradientHover2
import it.macgood.yandexcupmusicapp.ui.custom.DropdownButtonDefaults.gradientHover3


@Composable
fun DropdownButtonBox(
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int,
    @RawRes rawIds: List<Int>,
    sampleNames: List<String>,
    onClick: (Int, String) -> Unit
) {
    Box(modifier = Modifier.width(80.dp)) {
        DropdownButton(
            drawableId = drawableId,
            stringId = stringId,
            rawIds = rawIds,
            sampleNames = sampleNames,
            onClick = { int, str -> onClick(int, str) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownButton(
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int,
    @RawRes rawIds: List<Int>,
    sampleNames: List<String>,
    onClick: (Int, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val boxCoordinates = remember { mutableListOf(Offset.Zero, Offset.Zero, Offset.Zero) }
    val boxHovers = remember { mutableStateListOf(false, false, false) }

    Column(
        modifier = Modifier
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> { expanded = !expanded }
                    MotionEvent.ACTION_MOVE -> {
                        if (event.y < 700) {
                            boxCoordinates.forEachIndexed { index, boxPosition ->

                                if (event.y >= boxPosition.y - 18.dp.value && event.y <= boxPosition.y + 80.dp.value) {
                                    when (index) {
                                        0 -> {
                                            boxHovers[0] = true
                                            boxHovers[1] = false
                                            boxHovers[2] = false
                                        }

                                        1 -> {
                                            boxHovers[0] = false
                                            boxHovers[1] = true
                                            boxHovers[2] = false
                                        }

                                        2 -> {
                                            boxHovers[0] = false
                                            boxHovers[1] = false
                                            boxHovers[2] = true
                                        }
                                    }
                                }
                            }
                        } else {
                            boxHovers[0] = false
                            boxHovers[1] = false
                            boxHovers[2] = false
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        val indexOf = boxHovers.indexOf(true)
                        if (indexOf == -1) {
                            onClick(rawIds[0], sampleNames[0])
                        } else {
                            onClick(rawIds[indexOf], sampleNames[indexOf])
                        }

                        expanded = !expanded
                    }
                }
                true
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(if (expanded) YGreen else Color.White, CircleShape)
                .zIndex(3f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            expanded = !expanded
                        }
                    )
                },
            contentAlignment = if (drawableId == R.drawable.ic_guitar) Alignment.BottomCenter else Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = drawableId),
                contentDescription = null
            )
        }
        AnimatedVisibility(
            visible = !expanded
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stringResource(id = stringId), color = Color.White)
        }
        Column(
            modifier = Modifier
                .zIndex(2f)
                .background(Color.Transparent, RoundedCornerShape(16.dp))
        ) {
            if (expanded) {
                Box(
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .width(80.dp)
                        .height(72.dp)
                        .zIndex(2f)
                        .background(if (boxHovers[0]) gradientHover2 else gradientDefault)
                        .onGloballyPositioned {
                            boxCoordinates[0] = it.positionInWindow()
                        },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Text(text = "Сэмпл 1")
                }
                Box(
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .width(80.dp)
                        .height(56.dp)
                        .zIndex(2f)
                        .background(if (boxHovers[1]) gradientHover else gradientDefault)
                        .onGloballyPositioned {
                            boxCoordinates[1] = it.positionInWindow()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Сэмпл 2")
                }
                Box(
                    modifier = Modifier
                        .offset(y = (-40).dp)
                        .width(80.dp)
                        .height(80.dp)
                        .zIndex(2f)
                        .background(
                            if (boxHovers[2]) gradientHover3 else gradientDefault,
                            RoundedCornerShape(bottomStart = 48.dp, bottomEnd = 48.dp)
                        )
                        .onGloballyPositioned {
                            boxCoordinates[2] = it.positionInWindow()
                        },
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "Сэмпл 3")
                }
            }
        }
    }
}

private fun onMove(
    event: MotionEvent,
    boxCoordinates: MutableList<Offset>,
    boxHovers: SnapshotStateList<Boolean>
) {

}

object DropdownButtonDefaults {
    val gradientHover = Brush.verticalGradient(
        colors = listOf(YGreen, Color.White, Color.White, YGreen),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )
    val gradientHover2 = Brush.verticalGradient(
        colors = listOf(YGreen, YGreen, Color.White),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )
    val gradientHover3 = Brush.verticalGradient(
        colors = listOf(Color.White, YGreen, YGreen),
        startY = 0.0f,
        endY = Float.POSITIVE_INFINITY
    )

    val gradientDefault = Brush.horizontalGradient(
        colors = listOf(YGreen, YGreen),
        startX = 0.0f,
        endX = Float.POSITIVE_INFINITY
    )
}