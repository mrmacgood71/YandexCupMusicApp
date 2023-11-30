package it.macgood.core_ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object CustomTransition {
    fun enterTransition() =
        slideInHorizontally(tween(600)) { width -> width }

    fun exitTransition() =
        slideOutHorizontally(tween(600)) { width -> -width }

    fun popEnterTransition() =
        slideInHorizontally(tween(600)) { width -> -width }

    fun popExitTransition() =
        slideOutHorizontally(tween(600)) { width -> width }
}