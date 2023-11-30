package it.macgood.core_ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import it.macgood.core_ui.R
import kotlin.math.abs


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(images: List<String>) {

    val pagerState = rememberPagerState { images.size }
    val isLoading = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(272.dp)
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState
        ) {

            if (
                isLoading.value && !pagerState.isScrollInProgress
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(56.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    model = images[it],
                    error = painterResource(id = R.drawable.ic_broken_image_24),
                    onLoading = { isLoading.value = true },
                    onSuccess = { isLoading.value = false },
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null
                )
            }

        }

        HotelCarouselIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 240.dp)
                .wrapContentSize(Alignment.BottomCenter),
            countOfPages = images.size,
            currentPage = pagerState.currentPage
        )
    }
}


@Composable
fun HotelCarouselIndicator(
    modifier: Modifier = Modifier,
    countOfPages: Int,
    currentPage: Int
) {
    Row(
        modifier = modifier
            .width((countOfPages * 16 + 16).dp) // count of pages * (box size + right padding) + horizontalArrangement
            .height(20.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(countOfPages) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(1f / calculateAlpha(it, currentPage))
                    .background(
                        color = if (currentPage == it) Color.Black else Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )

            )
        }
    }
}

private fun calculateAlpha(currentCircle: Int, selectedCircle: Int): Int {
    val range = abs(currentCircle - selectedCircle)
    return if (range == 0) 1 else (range + 1)
}